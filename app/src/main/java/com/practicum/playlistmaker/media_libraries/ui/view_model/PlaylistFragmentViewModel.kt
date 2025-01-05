package com.practicum.playlistmaker.media_libraries.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_libraries.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.media_libraries.ui.state.PlaylistState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragmentViewModel(
    private val playlistId: Int,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private lateinit var playlist: Playlist
    private var tracks: MutableList<Track> = mutableListOf()
    private var tracksTime: Long = 0


    private val playlistStateLiveData = MutableLiveData<PlaylistState>()
    fun observePlaylistState(): LiveData<PlaylistState> = playlistStateLiveData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlist = playlistInteractor.getPlaylist(playlistId)
        }
            .invokeOnCompletion {
                viewModelScope.launch(Dispatchers.IO) {
                    playlistInteractor
                        .getPlaylistTracks(playlist.trackIdList)
                        .collect { track ->
                            tracks.addAll(track)
                        }
                }.invokeOnCompletion {
                    val timeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
                    for (track in tracks) {
                        tracksTime += timeFormat.parse(track.trackTime)!!.time
                    }
                    playlistStateLiveData.postValue(
                        PlaylistState(
                            playlist,
                            tracks,
                            SimpleDateFormat("mm", Locale.getDefault()).format(tracksTime).toInt()
                        )
                    )
                }
            }
    }

    fun deleteTrackFromPlaylist (trackId: Int) {

    }
}