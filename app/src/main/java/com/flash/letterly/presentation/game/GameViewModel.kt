package com.flash.letterly.presentation.game

import dagger.hilt.android.lifecycle.HiltViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flash.letterly.domain.model.createBoard
import com.flash.letterly.domain.usecase.CheckGameStatusUseCase
import com.flash.letterly.domain.usecase.CheckWordExistsUseCase
import com.flash.letterly.domain.usecase.EvaluateGuessUseCase
import com.flash.letterly.domain.usecase.GetRandomWordUseCase
import com.flash.letterly.domain.usecase.UpdateWordTimestampUseCase
import com.flash.letterly.domain.usecase.GameStatus
import com.flash.letterly.domain.usecase.CheckDuplicateGuessUseCase
import com.flash.letterly.domain.usecase.ClearRowUseCase
import com.flash.letterly.domain.usecase.ApplyGuessResultUseCase
import com.flash.letterly.domain.usecase.UpdateKeyboardStateUseCase
import com.flash.letterly.presentation.shared.GameMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getRandomWordUseCase: GetRandomWordUseCase,
    private val evaluateGuessUseCase: EvaluateGuessUseCase,
    private val updateWordTimestampUseCase: UpdateWordTimestampUseCase,
    private val checkGameStatusUseCase: CheckGameStatusUseCase,
    private val checkWordExistsUseCase: CheckWordExistsUseCase,
    private val checkDuplicateGuessUseCase: CheckDuplicateGuessUseCase,
    private val clearRowUseCase: ClearRowUseCase,
    private val applyGuessResultUseCase: ApplyGuessResultUseCase,
    private val updateKeyboardStateUseCase: UpdateKeyboardStateUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state
    private val _events = MutableSharedFlow<GameEvent>()
    val events = _events.asSharedFlow()

    private val guesses = mutableListOf<String>()

    fun startGame(mode: GameMode) {
        viewModelScope.launch {
            guesses.clear()
            val word = getRandomWordUseCase(mode) ?: return@launch

            val board = createBoard(
                wordLength = mode.wordLength,
                maxGuesses = mode.maxGuesses
            )

            _state.update {
                it.copy(
                    targetWord = word.value,
                    board = board,
                    currentRow = 0,
                    currentCol = 0,
                    gameMode = mode,
                    gameStatus = GameStatus.CONTINUE
                )
            }
        }
    }

    fun addLetter(letter: Char) {
        val state = _state.value

        if (state.currentCol >= state.gameMode.wordLength) return

        val board = state.board.map { it.toMutableList() }.toMutableList()
        val tile = board[state.currentRow][state.currentCol]
        board[state.currentRow][state.currentCol] = tile.copy(letter = letter)

        val newCol = state.currentCol + 1
        _state.update {
            it.copy(
                board = board,
                currentCol = newCol
            )
        }

        if (newCol == state.targetWord.length) {
            submitGuess()
        }
    }

    fun removeLetter() {
        val state = _state.value

        if (state.currentCol == 0) return

        val newCol = state.currentCol - 1
        val board = state.board.map { it.toMutableList() }.toMutableList()
        board[state.currentRow][newCol] = board[state.currentRow][newCol].copy(letter = null)

        _state.update {
            it.copy(
                board = board,
                currentCol = newCol
            )
        }
    }

    private fun submitGuess() {
        viewModelScope.launch {
            val state = _state.value

            if (state.currentCol < state.gameMode.wordLength) return@launch

            val guess = state.board[state.currentRow]
                .mapNotNull { it.letter }
                .joinToString("")

            if (checkDuplicateGuessUseCase(guess, guesses)) {
                val clearedBoard = clearRowUseCase(
                    board = state.board,
                    row = state.currentRow,
                    wordLength = state.gameMode.wordLength
                )
                _state.update { it.copy(board = clearedBoard, currentCol = 0) }
                _events.emit(GameEvent.DuplicateWord)
                return@launch
            }

            val exists = checkWordExistsUseCase(guess)
            if (!exists) {
                val clearedBoard = clearRowUseCase(
                    board = state.board,
                    row = state.currentRow,
                    wordLength = state.gameMode.wordLength
                )
                _state.update { it.copy(board = clearedBoard, currentCol = 0) }
                _events.emit(GameEvent.InvalidWord)
                return@launch
            }

            guesses.add(guess)

            val evaluation = evaluateGuessUseCase(
                guess = guess,
                target = state.targetWord
            )

            val board = applyGuessResultUseCase(
                board = state.board,
                row = state.currentRow,
                states = evaluation.states
            )

            val updatedKeyboard = updateKeyboardStateUseCase(
                keyboard = state.keyboard,
                guess = guess,
                states = evaluation.states
            )

            val status = checkGameStatusUseCase(
                guesses = guesses,
                targetWord = state.targetWord,
                maxGuesses = state.gameMode.maxGuesses
            )

            _state.update {
                it.copy(
                    board = board,
                    currentRow = state.currentRow + 1,
                    currentCol = 0,
                    gameStatus = status,
                    keyboard = updatedKeyboard
                )
            }

            if (status == GameStatus.WIN) {
                _events.emit(GameEvent.GameWon)
            } else if (status == GameStatus.LOSE) {
                _events.emit(GameEvent.GameLost(state.targetWord))
            }
        }
    }

    fun resetGame() {
        val mode = _state.value.gameMode
        guesses.clear()
        _state.update { GameState(gameMode = mode) }
        startGame(mode)
    }
}
