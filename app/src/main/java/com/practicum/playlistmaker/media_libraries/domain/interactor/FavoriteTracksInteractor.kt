package com.practicum.playlistmaker.media_libraries.domain.interactor

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun insertFavoriteTrack(track: Track)

    suspend fun deleteFavoriteTrack(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>
}