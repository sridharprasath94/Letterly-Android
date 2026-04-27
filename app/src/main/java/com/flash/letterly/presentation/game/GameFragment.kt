package com.flash.letterly.presentation.game

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
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
import com.flash.letterly.presentation.game.keyboard.clearKeyboard
import com.flash.letterly.presentation.game.keyboard.keyButtons
import com.flash.letterly.presentation.game.keyboard.updateKeyboard
import com.flash.letterly.presentation.utils.showCenteredSnackBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            gameModeSubtitle?.text = getString(
                R.string.game_mode_subtitle,
                args.gameMode.wordLength,
                args.gameMode.maxGuesses
            )

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
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.leave_game_title)
                        .setMessage(R.string.leave_game_message)
                        .setPositiveButton(R.string.leave_button) { _, _ ->
                            findNavController().popBackStack()
                        }
                        .setNegativeButton(R.string.stay_button, null)
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest { event ->
                    when (event) {
                        GameEvent.InvalidWord -> {
                            binding.root.showCenteredSnackBar(getString(R.string.word_not_in_dictionary))
                        }

                        GameEvent.DuplicateWord -> {
                            binding.root.showCenteredSnackBar(getString(R.string.word_already_guessed))
                        }

                        GameEvent.GameWon -> {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(R.string.game_won_title)
                                .setMessage(R.string.play_again_message)
                                .setPositiveButton(R.string.replay_button) { _, _ ->
                                    binding.keyboardView.clearKeyboard()
                                    viewModel.resetGame()
                                }
                                .setNegativeButton(R.string.back_button) { _, _ ->
                                    binding.keyboardView.clearKeyboard()
                                    findNavController().popBackStack()
                                }
                                .show()
                        }

                        is GameEvent.GameLost -> {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(R.string.game_lost_title)
                                .setMessage(getString(R.string.game_lost_message, event.target))
                                .setPositiveButton(R.string.replay_button) { _, _ ->
                                    binding.keyboardView.clearKeyboard()
                                    viewModel.resetGame()
                                }
                                .setNegativeButton(R.string.back_button) { _, _ ->
                                    binding.keyboardView.clearKeyboard()
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
