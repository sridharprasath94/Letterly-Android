package com.flash.letterly.presentation.game

import com.flash.letterly.domain.model.LetterState
import com.flash.letterly.domain.model.LetterTile
import com.flash.letterly.domain.usecase.GameStatus
import com.flash.letterly.presentation.shared.GameMode

data class GameState(
    val targetWord: String = "",
    val board: List<List<LetterTile>> = emptyList(),
    val currentRow: Int = 0,
    val currentCol: Int = 0,
    val gameMode: GameMode = GameMode.CLASSIC,
    val gameStatus: GameStatus = GameStatus.CONTINUE,
    val keyboard: Map<Char, LetterState> = emptyMap(),
    val hintState: HintState = HintState.Idle,
    val hintsUsed: Int = 0,
    val receivedHints: List<String> = emptyList()
)