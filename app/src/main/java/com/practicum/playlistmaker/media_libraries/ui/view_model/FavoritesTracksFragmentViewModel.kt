package com.practicum.playlistmaker.media_libraries.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_libraries.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.media_libraries.ui.state.FavoriteTracksState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class FavoritesTracksFragmentViewModel(private val favoriteTracksInteractor: FavoriteTracksInteractor) :
    ViewModel() {

    private val tracks: MutableList<Track> = mutableListOf()

    private val stateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeState(): LiveData<FavoriteTracksState> = stateLiveData

    init {
        getFavoriteTracks()
    }

    private fun getFavoriteTracks() {
        viewModelScope.launch {
            favoriteTracksInteractor
                .getFavoriteTracks()
                .collect { track ->
                    tracks.addAll(track)
                }
        }.invokeOnCompletion {
            if (tracks.isEmpty()) {
                stateLiveData.postValue(FavoriteTracksState.Empty)
            } else {
                stateLiveData.postValue(FavoriteTracksState.Content(tracks))
            }
        }
    }
}