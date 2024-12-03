package com.practicum.playlistmaker.media_libraries.domain.repository

import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    
    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlistId: Int)

    fun getPlaylist(): Flow<List<Playlist>>

    suspend fun updatePlaylist(playlist: Playlist)
}