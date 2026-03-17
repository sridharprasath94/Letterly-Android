package com.flash.letterly.presentation.game.keyboard

import android.content.res.ColorStateList
import android.widget.Button
import com.flash.letterly.R
import com.flash.letterly.databinding.ViewKeyboardBinding
import com.flash.letterly.domain.model.LetterState

fun ViewKeyboardBinding.keyButtons(): Map<Char, Button> {
    return mapOf(
        'Q' to keyQ, 'W' to keyW, 'E' to keyE, 'R' to keyR, 'T' to keyT,
        'Y' to keyY, 'U' to keyU, 'I' to keyI, 'O' to keyO, 'P' to keyP,
        'A' to keyA, 'S' to keyS, 'D' to keyD, 'F' to keyF, 'G' to keyG,
        'H' to keyH, 'J' to keyJ, 'K' to keyK, 'L' to keyL,
        'Z' to keyZ, 'X' to keyX, 'C' to keyC, 'V' to keyV,
        'B' to keyB, 'N' to keyN, 'M' to keyM
    )
}

fun ViewKeyboardBinding.clearKeyboard() {
    val buttons = keyButtons()

    buttons.values.forEach { button ->
        val context = button.context
        button.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.md_theme_surface))
        button.setTextColor(ColorStateList.valueOf(context.getColor(R.color.md_theme_onSurface)))
    }
}

fun ViewKeyboardBinding.updateKeyboard(
    keyboard: Map<Char, LetterState>
) {
    val buttons = keyButtons()

    keyboard.forEach { (char, state) ->
        val button = buttons[char.uppercaseChar()] ?: return@forEach
        button.backgroundTintList = ColorStateList.valueOf(state.bgColor)
    }
}