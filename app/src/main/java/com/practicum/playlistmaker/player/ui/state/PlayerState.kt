package com.practicum.playlistmaker.player.ui.state

interface PlayerState {

    data object StatePrepared : PlayerState

    data object StatePaused : PlayerState

    data class StatePlaying(
        val currentPosition: Int,
    ) : PlayerState
}
