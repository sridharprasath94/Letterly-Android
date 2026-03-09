package com.flash.letterly.presentation.game

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.flash.letterly.R
import com.flash.letterly.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.flash.letterly.domain.model.LetterState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.core.graphics.toColorInt

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

        val keys = listOf(
            binding.keyboardView.keyQ,
            binding.keyboardView.keyW,
            binding.keyboardView.keyE,
            binding.keyboardView.keyR,
            binding.keyboardView.keyT,
            binding.keyboardView.keyY,
            binding.keyboardView.keyU,
            binding.keyboardView.keyI,
            binding.keyboardView.keyO,
            binding.keyboardView.keyP,

            binding.keyboardView.keyA,
            binding.keyboardView.keyS,
            binding.keyboardView.keyD,
            binding.keyboardView.keyF,
            binding.keyboardView.keyG,
            binding.keyboardView.keyH,
            binding.keyboardView.keyJ,
            binding.keyboardView.keyK,
            binding.keyboardView.keyL,

            binding.keyboardView.keyZ,
            binding.keyboardView.keyX,
            binding.keyboardView.keyC,
            binding.keyboardView.keyV,
            binding.keyboardView.keyB,
            binding.keyboardView.keyN,
            binding.keyboardView.keyM
        )

        keys.forEach { button ->
            button.setOnClickListener {
                val letter = button.text.toString()[0]
                viewModel.addLetter(letter)
            }
        }

        binding.keyboardView.keyDelete.setOnClickListener {
            viewModel.removeLetter()
        }

//        binding.keyboardView.keyEnter.setOnClickListener {
//            viewModel.submitGuess()
//        }
    }
}

private fun com.flash.letterly.databinding.ViewKeyboardBinding.updateKeyboard(
    keyboard: Map<Char, LetterState>
) {
    val buttons = mapOf(
        'Q' to keyQ, 'W' to keyW, 'E' to keyE, 'R' to keyR, 'T' to keyT,
        'Y' to keyY, 'U' to keyU, 'I' to keyI, 'O' to keyO, 'P' to keyP,
        'A' to keyA, 'S' to keyS, 'D' to keyD, 'F' to keyF, 'G' to keyG,
        'H' to keyH, 'J' to keyJ, 'K' to keyK, 'L' to keyL,
        'Z' to keyZ, 'X' to keyX, 'C' to keyC, 'V' to keyV,
        'B' to keyB, 'N' to keyN, 'M' to keyM
    )

    keyboard.forEach { (char, state) ->
        val button = buttons[char.uppercaseChar()] ?: return@forEach

        val color = when (state) {
            LetterState.CORRECT -> "#6AAA64".toColorInt()
            LetterState.PRESENT -> "#C9B458".toColorInt()
            LetterState.ABSENT -> "#787C7E".toColorInt()
            LetterState.EMPTY -> return@forEach
        }

        button.setBackgroundColor(color)
    }
}