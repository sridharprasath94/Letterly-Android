package com.flash.letterly.presentation.game

sealed class GameEvent {
    object InvalidWord : GameEvent()
    object DuplicateWord : GameEvent()
    object GameWon : GameEvent()
    data class GameLost(val target: String) : GameEvent()
    data class HintReceived(val hint: String) : GameEvent()
    object HintFailed : GameEvent()
}