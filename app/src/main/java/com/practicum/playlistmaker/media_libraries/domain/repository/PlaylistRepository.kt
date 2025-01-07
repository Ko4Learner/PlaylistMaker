package com.practicum.playlistmaker.media_libraries.domain.repository

import android.net.Uri
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    
    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun deleteTrack(playlist: Playlist, trackId: Int)

    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylistTracks(tracksIdList: List<Int>): Flow<List<Track>>

    suspend fun getPlaylist(playlistId: Int): Playlist

    suspend fun updatePlaylist(playlist: Playlist, track: Track)

    suspend fun saveImageToPrivateStorage (uri: Uri, playlistName: String) : String
}