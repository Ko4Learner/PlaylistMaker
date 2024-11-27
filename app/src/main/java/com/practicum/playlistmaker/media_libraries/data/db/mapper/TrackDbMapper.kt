package com.practicum.playlistmaker.media_libraries.data.db.mapper

import com.practicum.playlistmaker.media_libraries.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.model.Track
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TrackDbMapper {

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")

    fun map(track: Track): TrackEntity {
        val current = LocalDateTime.now()
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
            current.format(formatter)
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
