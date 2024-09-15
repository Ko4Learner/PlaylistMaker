package com.practicum.playlistmaker.domain.use_case.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TrackPlayerRepository

class TrackPlayerInteractorImpl(private val repository: TrackPlayerRepository) :
    TrackPlayerInteractor {

    override fun preparePlayer(track: Track) {
        repository.preparePlayer(track)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun getCurrentPositionMediaPlayer(): Int {
        return repository.getCurrentPositionMediaPlayer()
    }

    override fun getMediaPlayer(): MediaPlayer {
        return repository.getMediaPlayer()
    }
}