package com.practicum.playlistmaker.media_libraries.data.db.mapper

import com.google.gson.Gson
import com.practicum.playlistmaker.media_libraries.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist

class UpdatePlaylistTrackListMapper(private val gson: Gson) {
    fun insertTrackMap(playlist: Playlist, trackId: Int): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.name,
            playlist.description,
            playlist.imagePath,
            gson.toJson(playlist.trackIdList + trackId),
            playlist.tracksCount + 1
        )
    }

    fun deleteTrackMap(playlist: Playlist, trackId: Int): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.name,
            playlist.description,
            playlist.imagePath,
            gson.toJson(playlist.trackIdList - trackId),
            playlist.tracksCount - 1
        )
    }
}