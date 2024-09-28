package com.practicum.playlistmaker.player.domain.repository

import com.practicum.playlistmaker.search.domain.model.Track

interface TrackPlayerRepository {
    fun preparePlayer(
        track: Track,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    )

    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPositionMediaPlayer(): Int
}