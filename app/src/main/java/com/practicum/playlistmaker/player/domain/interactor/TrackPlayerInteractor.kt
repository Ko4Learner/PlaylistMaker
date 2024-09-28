package com.practicum.playlistmaker.player.domain.interactor

import com.practicum.playlistmaker.search.domain.model.Track

interface TrackPlayerInteractor {
    fun preparePlayer(
        track: Track,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit,
    )

    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPositionMediaPlayer(): Int
}