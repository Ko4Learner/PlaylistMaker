package com.practicum.playlistmaker.media_libraries.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String, // mm:ss
    val artworkUrl100: String,
    val previewUrl: String,
    val collectionName: String, // если есть
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
)
