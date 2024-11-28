package com.practicum.playlistmaker.media_libraries.domain.interactor

import com.practicum.playlistmaker.media_libraries.domain.repository.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
):FavoriteTracksInteractor {

    override suspend fun insertFavoriteTrack(track: Track) {
        favoriteTracksRepository.insertFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(trackId: Int) {
        favoriteTracksRepository.deleteFavoriteTrack(trackId)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracks()
    }
}