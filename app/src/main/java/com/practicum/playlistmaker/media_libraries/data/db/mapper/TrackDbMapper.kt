package com.practicum.playlistmaker.media_libraries.data.db.mapper

import com.practicum.playlistmaker.media_libraries.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.model.Track

object TrackDbMapper {

    fun map(track: Track, time: String): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.previewUrl,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            time
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.previewUrl,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country
        )
    }
}
