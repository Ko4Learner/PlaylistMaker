package com.practicum.playlistmaker.media_libraries.data

import com.practicum.playlistmaker.media_libraries.data.db.FavoriteTracksDatabase
import com.practicum.playlistmaker.media_libraries.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media_libraries.data.db.mapper.TrackDbMapper
import com.practicum.playlistmaker.media_libraries.domain.repository.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FavoriteTracksRepositoryImpl(
    private val favoriteTracksDatabase: FavoriteTracksDatabase
) : FavoriteTracksRepository {

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")

    override suspend fun insertFavoriteTrack(track: Track) {
        val current = LocalDateTime.now()
        favoriteTracksDatabase.trackDao()
            .insertFavoriteTrack(TrackDbMapper.map(track, current.format(formatter)))
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        favoriteTracksDatabase.trackDao().deleteFavoriteTrack(TrackDbMapper.map(track, ""))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val favoriteTracks = favoriteTracksDatabase.trackDao().getFavoriteTracks()
        emit(convertFromTrackEntity(favoriteTracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.sortedBy { LocalDate.parse(it.addTime, formatter) }
            .map { track -> TrackDbMapper.map(track) }
    }
}