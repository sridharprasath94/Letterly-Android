package com.flash.letterly.presentation.game.keyboard

import android.widget.Button

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
                onLetterPressed(letter)
            }
        }

        deleteButton.setOnClickListener {
            onDeletePressed()
        }
    }
}