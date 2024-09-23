package com.practicum.playlistmaker.player.domain.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.search.domain.model.Track

interface TrackPlayerRepository {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPositionMediaPlayer(): Int
    fun getMediaPlayer(): MediaPlayer
}