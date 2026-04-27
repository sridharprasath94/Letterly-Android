package com.flash.letterly.presentation.game

sealed class HintState {
    object Idle : HintState()
    object Loading : HintState()
}
