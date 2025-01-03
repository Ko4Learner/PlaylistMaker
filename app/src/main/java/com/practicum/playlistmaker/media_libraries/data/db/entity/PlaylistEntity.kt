package com.practicum.playlistmaker.media_libraries.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val name: String,
    val description: String,
    val imagePath: String,
    val trackIdList: String,
    val tracksCount: Int
)
