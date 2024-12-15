package com.practicum.playlistmaker.media_libraries.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media_libraries.data.db.dao.TrackDao
import com.practicum.playlistmaker.media_libraries.data.db.entity.TrackEntity

@Database(entities = [TrackEntity::class], version = 2)
abstract class FavoriteTracksDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
}