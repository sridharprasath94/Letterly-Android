package com.flash.letterly.presentation.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView

fun View.showCenteredSnackBar(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)

    val textView = snackBar.view.findViewById<MaterialTextView>(
        com.google.android.material.R.id.snackbar_text
    )
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    textView.gravity = android.view.Gravity.CENTER

    snackBar.show()
}