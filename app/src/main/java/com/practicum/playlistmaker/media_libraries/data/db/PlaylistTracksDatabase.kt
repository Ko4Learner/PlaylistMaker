package com.practicum.playlistmaker.media_libraries.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media_libraries.data.db.dao.PlaylistTracksDao
import com.practicum.playlistmaker.media_libraries.data.db.entity.TrackEntity

@Database(entities = [TrackEntity::class], version = 1)
abstract class PlaylistTracksDatabase : RoomDatabase() {
    abstract fun playlistTracksDao(): PlaylistTracksDao
}