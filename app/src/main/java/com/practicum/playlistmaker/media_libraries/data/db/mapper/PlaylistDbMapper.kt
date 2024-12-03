package com.practicum.playlistmaker.media_libraries.data.db.mapper

import com.google.gson.Gson
import com.practicum.playlistmaker.media_libraries.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist


class PlaylistDbMapper {

    private val gson = Gson()

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.name,
            playlist.description,
            playlist.imagePath,
            gson.toJson(playlist.trackIdList),
            playlist.tracksCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.playlistId,
            playlist.name,
            playlist.description,
            playlist.imagePath,
            gson.fromJson(playlist.trackIdList, Array<Int>::class.java).asList(),
            playlist.tracksCount
        )
    }
}