package com.practicum.playlistmaker.player.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.interactor.TrackPlayerInteractor
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(
    private val track: Track,
    private val trackPlayerInteractor: TrackPlayerInteractor,
) : ViewModel() {

    private var playerState = STATE_DEFAULT

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private var timerJob: Job? = null

    init {
        preparePlayer()
    }

    fun startPlaying() {
        playbackControl()
        startTimer()
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
        timerJob?.cancel()
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

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerState == STATE_PLAYING) {
                delay(REFRESH_LISTENED_TIME_DELAY_MILLIS)
                renderState(PlayerState.StatePlaying(trackPlayerInteractor.getCurrentPositionMediaPlayer()))
            }
        }
    }

    override fun onCleared() {
        trackPlayerInteractor.releasePlayer()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val REFRESH_LISTENED_TIME_DELAY_MILLIS = 300L
    }
}