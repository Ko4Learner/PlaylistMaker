package com.practicum.playlistmaker.search.data.mapper

import com.practicum.playlistmaker.search.data.dto.TrackStorageDto
import com.practicum.playlistmaker.search.data.dto.TracksResponse
import com.practicum.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TracksMapper {
    fun mapTrackResponse(response: TracksResponse): List<Track> {
        return response.results.map {
            Track(
                it.trackId,
                it.trackName,
                it.artistName,
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(it.trackTime),
                it.artworkUrl100,
                it.previewUrl,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country
            )
        }
    }

    fun mapTrackStorage(tracksStorage: MutableList<TrackStorageDto>): MutableList<Track> {
        return tracksStorage.map {
            Track(
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTime,
                it.artworkUrl100,
                it.previewUrl,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country
            )
        }.toMutableList()
    }

    fun trackToTrackStorageDto(track: Track): TrackStorageDto {
        return TrackStorageDto(
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
