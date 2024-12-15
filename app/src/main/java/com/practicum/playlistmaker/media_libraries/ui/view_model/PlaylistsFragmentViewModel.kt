package com.practicum.playlistmaker.media_libraries.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_libraries.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.media_libraries.ui.state.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(private val playlistInteractor: PlaylistInteractor) :
    ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylist()
                .collect { playlist ->
                    val playlists = mutableListOf<Playlist>()
                    playlists.addAll(playlist)

                    if (playlists.isEmpty()) {
                        renderState(PlaylistsState.Empty)
                    } else renderState(PlaylistsState.Content(playlists))
                }
        }
    }

    private fun renderState(state: PlaylistsState) {
        stateLiveData.postValue(state)
    }

}