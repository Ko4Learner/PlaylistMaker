package com.practicum.playlistmaker.media_libraries.domain.repository

import android.net.Uri
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    
    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlistId: Int)

    fun getPlaylist(): Flow<List<Playlist>>

    suspend fun updatePlaylist(playlist: Playlist, track: Track)

    suspend fun saveImageToPrivateStorage (uri: Uri, playlistName: String) : String
}