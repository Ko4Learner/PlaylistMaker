package com.practicum.playlistmaker.media_libraries.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_libraries.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistFragmentViewModel(
    private val playlistId: Int,
    private val playlistInteractor: PlaylistInteractor
) :
    NewPlaylistFragmentViewModel(playlistInteractor) {

    private var playlist: Playlist? = null

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistLiveData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlist = playlistInteractor.getPlaylist(playlistId)
            playlistLiveData.postValue(playlist!!)
        }
    }

    fun editPlaylist(name: String, description: String, imagePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.editPlaylist(
                Playlist(
                    playlistId = playlist!!.playlistId,
                    name = name,
                    description = description,
                    imagePath = imagePath, tracksCount = playlist!!.tracksCount,
                    trackIdList = playlist!!.trackIdList
                )
            )
        }
    }

}