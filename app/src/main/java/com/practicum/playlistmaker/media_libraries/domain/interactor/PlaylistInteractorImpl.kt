package com.practicum.playlistmaker.media_libraries.domain.interactor

import android.net.Uri
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.media_libraries.domain.repository.PlaylistRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistRepository.insertPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistRepository.deletePlaylist(playlistId)
    }

    override fun getPlaylist(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylist()
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) {
        playlistRepository.updatePlaylist(playlist, track)
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri, playlistName: String): String {
        return playlistRepository.saveImageToPrivateStorage(uri, playlistName)
    }
}