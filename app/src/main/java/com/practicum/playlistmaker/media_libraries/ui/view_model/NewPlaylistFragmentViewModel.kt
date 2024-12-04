package com.practicum.playlistmaker.media_libraries.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_libraries.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import kotlinx.coroutines.launch

class NewPlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor) :
    ViewModel() {

    fun insertNewPlaylist(name: String, description: String, imagePath: String) {
        viewModelScope.launch {
            playlistInteractor.insertPlaylist(
                Playlist(
                    name = name,
                    description = description,
                    imagePath = imagePath
                )
            )
        }
    }
}