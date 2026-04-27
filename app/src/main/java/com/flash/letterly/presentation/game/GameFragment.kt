package com.flash.letterly.presentation.game

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
                GridLayoutManager(requireContext(), args.gameMode.wordLength)
            boardRecyclerView.adapter = boardAdapter
            setupKeyboard()
            setupHintButton()

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
                GridSpacingItemDecoration(spanCount = span, spacing = 30, includeEdge = false)
            )
        }

        if (viewModel.state.value.board.isEmpty()) {
            viewModel.startGame(args.gameMode)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    boardAdapter.submitBoard(state.board)
                    val currentRowStart = state.currentRow * args.gameMode.wordLength
                    binding.boardRecyclerView.scrollToPosition(currentRowStart)
                    binding.keyboardView.updateKeyboard(state.keyboard)
                    updateHintButton(state.hintsUsed, state.gameMode.maxHints, state.hintState)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest { event ->
                    when (event) {
                        GameEvent.InvalidWord ->
                            binding.root.showCenteredSnackBar(getString(R.string.word_not_in_dictionary))

                        GameEvent.DuplicateWord ->
                            binding.root.showCenteredSnackBar(getString(R.string.word_already_guessed))

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

                        is GameEvent.HintReceived -> {
                            val state = viewModel.state.value
                            val remaining = state.gameMode.maxHints - state.hintsUsed
                            val message = event.hints.mapIndexed { index, hint ->
                                "${getString(R.string.hint_label, index + 1)}: $hint"
                            }.joinToString("\n\n")
                            val builder = MaterialAlertDialogBuilder(requireContext())
                                .setTitle(R.string.hint_dialog_title)
                                .setMessage(message)
                            if (remaining > 0) {
                                builder
                                    .setPositiveButton(R.string.get_next_hint) { _, _ -> requestHint() }
                                    .setNegativeButton(R.string.close, null)
                            } else {
                                builder.setPositiveButton(R.string.close, null)
                            }
                            builder.show()
                        }

                        GameEvent.HintFailed -> {
                            binding.root.showCenteredSnackBar(getString(R.string.hint_error))
                        }
                    }
                }
            }
        }
    }

    private fun setupHintButton() {
        binding.hintButton?.setOnClickListener {
            val state = viewModel.state.value
            if (state.receivedHints.isEmpty()) {
                requestHint()
            } else {
                viewModel.showHints()
            }
        }
    }

    private fun requestHint() {
        if (isNetworkAvailable()) viewModel.requestHint()
        else binding.root.showCenteredSnackBar(getString(R.string.no_internet))
    }

    private fun updateHintButton(hintsUsed: Int, maxHints: Int, hintState: HintState) {
        val remaining = maxHints - hintsUsed
        val isLoading = hintState == HintState.Loading
        binding.hintButton?.apply {
            isEnabled = !isLoading && (remaining > 0 || hintsUsed > 0)
            alpha = if (remaining > 0) 1.0f else 0.4f
            setImageResource(
                if (remaining > 0) R.drawable.ic_bulb_filled else R.drawable.ic_bulb_outline
            )
        }
        binding.hintCountText?.text = getString(R.string.hint_count, remaining)
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork ?: return false) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun setupKeyboard() {
        val controller = KeyboardController(
            onLetterPressed = { viewModel.addLetter(it) },
            onDeletePressed = { viewModel.removeLetter() }
        )
        controller.bindKeys(
            buttons = binding.keyboardView.keyButtons(),
            deleteButton = binding.keyboardView.keyDelete
        )
    }
}
