package com.practicum.playlistmaker.media_libraries.domain.interactor

import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlistId: Int)

    fun getPlaylist(): Flow<List<Playlist>>

    suspend fun updatePlaylist(playlist: Playlist)
}