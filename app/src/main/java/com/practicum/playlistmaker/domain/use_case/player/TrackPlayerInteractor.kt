package com.practicum.playlistmaker.domain.use_case.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.model.Track

interface TrackPlayerInteractor {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPositionMediaPlayer(): Int
    fun getMediaPlayer(): MediaPlayer
}