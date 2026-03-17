package com.flash.letterly.presentation.game.keyboard

import android.widget.Button
import android.view.HapticFeedbackConstants

class KeyboardController(
    private val onLetterPressed: (Char) -> Unit,
    private val onDeletePressed: () -> Unit
) {

    fun bindKeys(
        buttons: Map<Char, Button>,
        deleteButton: Button
    ) {
        buttons.forEach { (letter, button) ->
            button.setOnClickListener {
                button.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                onLetterPressed(letter)
            }
        }

        deleteButton.setOnClickListener {
            deleteButton.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            onDeletePressed()
        }
    }
}