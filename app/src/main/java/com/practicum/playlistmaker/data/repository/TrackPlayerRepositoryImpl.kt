package com.practicum.playlistmaker.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TrackPlayerRepository


class TrackPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : TrackPlayerRepository {

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPositionMediaPlayer(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getMediaPlayer() = mediaPlayer

}