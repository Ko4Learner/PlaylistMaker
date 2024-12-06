package com.practicum.playlistmaker.media_libraries.data

import com.practicum.playlistmaker.media_libraries.data.db.PlaylistDatabase
import com.practicum.playlistmaker.media_libraries.data.db.PlaylistTracksDatabase
import com.practicum.playlistmaker.media_libraries.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_libraries.data.db.mapper.PlaylistDbMapper
import com.practicum.playlistmaker.media_libraries.data.db.mapper.TrackDbMapper
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.media_libraries.domain.repository.PlaylistRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDatabase: PlaylistDatabase,
    private val playlistDbMapper: PlaylistDbMapper,
    private val playlistTracksDatabase: PlaylistTracksDatabase,
    private val trackDbMapper: TrackDbMapper
) : PlaylistRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao()
            .insertPlaylist(playlistDbMapper.map(playlist))
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDatabase.playlistDao()
            .deletePlaylist(playlistId)
    }

    override fun getPlaylist(): Flow<List<Playlist>> = flow {
        val playlists = playlistDatabase.playlistDao().getPlaylist()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) {
        playlistDatabase.playlistDao()
            .updatePlaylist(playlistDbMapper.map(playlist))
        playlistTracksDatabase.playlistTracksDao().insertPlaylistTrack(trackDbMapper.map(track))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbMapper.map(playlist) }
    }
}