package com.flash.letterly.presentation.game

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.flash.letterly.R
import com.flash.letterly.databinding.FragmentGameBinding
import com.flash.letterly.domain.usecase.GameStatus
import com.flash.letterly.presentation.game.keyboard.KeyboardController
import com.flash.letterly.presentation.game.keyboard.keyButtons
import com.flash.letterly.presentation.game.keyboard.updateKeyboard
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : Fragment(R.layout.fragment_game) {
    private val binding: FragmentGameBinding by viewBinding(FragmentGameBinding::bind)
    private val viewModel: GameViewModel by viewModels()
    private val boardAdapter = BoardAdapter()
    private val args: GameFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            val span = args.gameMode.wordLength
            gameModeTextView.text = args.gameMode.toString()

            boardRecyclerView.layoutManager =
                GridLayoutManager(
                    requireContext(),
                    args.gameMode.wordLength,
                )

            boardRecyclerView.adapter = boardAdapter
            setupKeyboard()

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                val state = viewModel.state.value

                if (state.gameStatus == GameStatus.CONTINUE) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Leave game?")
                        .setMessage("Your current game will be lost. Do you want to go back?")
                        .setPositiveButton("Leave") { _, _ ->
                            findNavController().popBackStack()
                        }
                        .setNegativeButton("Stay", null)
                        .show()
                } else {
                    findNavController().popBackStack()
                }
            }

            boardRecyclerView.addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount = span,
                    spacing = 30, // px
                    includeEdge = false
                )
            )
        }

        // Start the game with the selected mode only if board is empty (prevents restart on config change)
        if (viewModel.state.value.board.isEmpty()) {
            viewModel.startGame(args.gameMode)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                // Update board
                boardAdapter.submitBoard(state.board)

                // Ensure current row is visible
                val span = args.gameMode.wordLength
                val currentRowStart = state.currentRow * span
                binding.boardRecyclerView.scrollToPosition(currentRowStart)

                // Update keyboard colors
                binding.keyboardView.updateKeyboard(state.keyboard)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest { event ->
                    when (event) {
                        GameEvent.InvalidWord -> {
                            Toast.makeText(
                                requireContext(),
                                "Word not in dictionary",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        GameEvent.DuplicateWord -> {
                            Toast.makeText(
                                requireContext(),
                                "Word has already been guessed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        GameEvent.GameWon -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("You won! 🎉")
                                .setMessage("Play again?")
                                .setPositiveButton("Replay") { _, _ ->
                                    viewModel.resetGame()
                                }
                                .setNegativeButton("Back") { _, _ ->
                                    findNavController().popBackStack()
                                }
                                .show()
                        }

                        is GameEvent.GameLost -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("You lost! 😢")
                                .setMessage("The word was: ${event.target}\nPlay again?")
                                .setPositiveButton("Replay") { _, _ ->
                                    viewModel.resetGame()
                                }
                                .setNegativeButton("Back") { _, _ ->
                                    findNavController().popBackStack()
                                }
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun setupKeyboard() {
        val controller = KeyboardController(
            onLetterPressed = { letter ->
                viewModel.addLetter(letter)
            },
            onDeletePressed = {
                viewModel.removeLetter()
            }
        )

        controller.bindKeys(
            buttons = binding.keyboardView.keyButtons(),
            deleteButton = binding.keyboardView.keyDelete
        )
    }
}