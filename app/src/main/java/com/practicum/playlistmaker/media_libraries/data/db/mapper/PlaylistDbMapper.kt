package com.practicum.playlistmaker.media_libraries.data.db.mapper

import com.google.gson.Gson
import com.practicum.playlistmaker.media_libraries.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track


class PlaylistDbMapper(private val gson: Gson) {

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

    fun stringToList(stringList: String): List<Int> {
        return gson.fromJson(stringList, Array<Int>::class.java).asList()
    }

    fun insertTrackMap(playlist: Playlist, track: Track): PlaylistEntity {
        val trackCount = playlist.tracksCount + 1
        val trackIdList: List<Int> =
            playlist.trackIdList.toMutableList().apply { add(track.trackId) }
        return PlaylistEntity(
            playlist.playlistId,
            playlist.name,
            playlist.description,
            playlist.imagePath,
            gson.toJson(trackIdList),
            trackCount
        )
    }

    fun deleteTrackMap(playlist: Playlist, trackId: Int): PlaylistEntity {
        val trackCount = playlist.tracksCount - 1
        val trackIdList: List<Int> = playlist.trackIdList.toMutableList().apply { remove(trackId) }
        return PlaylistEntity(
            playlist.playlistId,
            playlist.name,
            playlist.description,
            playlist.imagePath,
            gson.toJson(trackIdList),
            trackCount
        )
    }
}