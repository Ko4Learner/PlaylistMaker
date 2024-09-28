package com.practicum.playlistmaker.player.domain.interactor

import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.player.domain.repository.TrackPlayerRepository

class TrackPlayerInteractorImpl(private val repository: TrackPlayerRepository) :
    TrackPlayerInteractor {

    override fun preparePlayer(
        track: Track,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit,
    ) {
        repository.preparePlayer(track, { onPreparedListener() }, { onCompletionListener() })
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
}