package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.player.domain.interactor.TrackPlayerInteractor
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.search.domain.model.Track


class PlayerViewModel(
    private val track: Track,
    private val trackPlayerInteractor: TrackPlayerInteractor,
) : ViewModel() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val REFRESH_LISTENED_TIME_DELAY_MILLIS = 500L
        private val PLAYER_REQUEST_TOKEN = Any()

        fun factory(
            track: Track,
            trackPlayerInteractor: TrackPlayerInteractor,
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    PlayerViewModel(track, trackPlayerInteractor)
                }
            }
        }
    }

    private var playerState = STATE_DEFAULT

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    init {
        preparePlayer()
    }

    private val handler = Handler(Looper.getMainLooper())

    fun startPlaying() {

        playbackControl()

        handler.postDelayed(
            object : Runnable {
                override fun run() {

                    when (playerState) {
                        STATE_PLAYING -> {
                            renderState(PlayerState.StatePlaying(trackPlayerInteractor.getCurrentPositionMediaPlayer()))
                            handler.postDelayed(
                                this, REFRESH_LISTENED_TIME_DELAY_MILLIS
                            )
                        }

                        STATE_DEFAULT, STATE_PREPARED -> {
                            handler.removeCallbacksAndMessages(PLAYER_REQUEST_TOKEN)
                        }

                        STATE_PAUSED -> handler.removeCallbacksAndMessages(
                            PLAYER_REQUEST_TOKEN
                        )
                    }
                }
            }, REFRESH_LISTENED_TIME_DELAY_MILLIS
        )
    }

    private fun preparePlayer() {
        playerState = STATE_PREPARED
        trackPlayerInteractor.preparePlayer(track, {
            renderState(PlayerState.StatePrepared)
            playerState = STATE_PREPARED
        }, {
            renderState(PlayerState.StatePrepared)
            playerState = STATE_PREPARED
        })
    }

    private fun startPlayer() {
        playerState = STATE_PLAYING
        trackPlayerInteractor.startPlayer()
    }

    private fun pausePlayer() {
        playerState = STATE_PAUSED
        renderState(PlayerState.StatePaused)
        trackPlayerInteractor.pausePlayer()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PAUSED, STATE_PREPARED -> startPlayer()
            STATE_PLAYING -> pausePlayer()
        }
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }

    fun onPauseActivity() {
        if (playerState == STATE_PLAYING) {
            pausePlayer()
        }
    }

    override fun onCleared() {
        trackPlayerInteractor.releasePlayer()
        handler.removeCallbacksAndMessages(PLAYER_REQUEST_TOKEN)
    }
}