package com.practicum.playlistmaker.media_libraries.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_libraries.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class NewPlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor) :
    ViewModel() {

    private val imagePathLiveData = MutableLiveData<String>()
    fun observeImagePath(): LiveData<String> = imagePathLiveData

    fun insertNewPlaylist(name: String, description: String, imagePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.insertPlaylist(
                Playlist(
                    name = name,
                    description = description,
                    imagePath = imagePath
                )
            )
        }
    }

    fun saveImageToPrivateStorage (uri: Uri, playlistName: String){
        viewModelScope.launch(Dispatchers.IO) {
            imagePathLiveData.postValue(playlistInteractor.saveImageToPrivateStorage(uri,playlistName))
        }
    }

}