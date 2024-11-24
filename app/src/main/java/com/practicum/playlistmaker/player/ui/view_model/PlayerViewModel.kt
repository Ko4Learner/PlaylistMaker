package com.practicum.playlistmaker.player.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_libraries.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.player.domain.interactor.TrackPlayerInteractor
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val trackPlayerInteractor: TrackPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private var playerState = STATE_DEFAULT

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData

    private var timerJob: Job? = null

    init {
        preparePlayer()
        isFavoriteLiveData.postValue(track.isFavorite)
    }

    fun startPlaying() {
        playbackControl()
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
        startTimer()
    }

    private fun pausePlayer() {
        playerState = STATE_PAUSED
        trackPlayerInteractor.pausePlayer()
        timerJob?.cancel()
        renderState(PlayerState.StatePaused)
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

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (!track.isFavorite) {
                favoriteTracksInteractor.insertFavoriteTrack(track)
                track.isFavorite = true
            } else {
                favoriteTracksInteractor.deleteFavoriteTrack(track)
                track.isFavorite = false
            }
            isFavoriteLiveData.postValue(track.isFavorite)
        }

    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerState == STATE_PLAYING) {
                renderState(PlayerState.StatePlaying(trackPlayerInteractor.getCurrentPositionMediaPlayer()))
                delay(REFRESH_LISTENED_TIME_DELAY_MILLIS)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
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