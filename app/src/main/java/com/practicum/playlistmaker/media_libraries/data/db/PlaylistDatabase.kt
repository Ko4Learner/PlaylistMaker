package com.practicum.playlistmaker.media_libraries.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media_libraries.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.media_libraries.data.db.entity.PlaylistEntity


@Database(entities = [PlaylistEntity::class], version = 1)
abstract class PlaylistDatabase : RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao
}