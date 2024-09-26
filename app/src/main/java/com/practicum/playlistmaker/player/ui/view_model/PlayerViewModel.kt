package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.player.domain.interactor.TrackPlayerInteractor


class PlayerViewModel(private val trackPlayerInteractor: TrackPlayerInteractor) : ViewModel() {

    companion object {

        private const val TRACK = "Track"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val REFRESH_LISTENED_TIME_DELAY_MILLIS = 500L
        private val PLAYER_REQUEST_TOKEN = Any()

        fun factory(
            trackPlayerInteractor: TrackPlayerInteractor,
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    PlayerViewModel(trackPlayerInteractor)
                }
            }
        }
    }

    private var playerState = STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())


    override fun onCleared() {
        handler.removeCallbacksAndMessages(PLAYER_REQUEST_TOKEN)
    }
}