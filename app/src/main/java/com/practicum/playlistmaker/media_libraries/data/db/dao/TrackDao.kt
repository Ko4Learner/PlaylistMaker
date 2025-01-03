package com.practicum.playlistmaker.media_libraries.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media_libraries.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track: TrackEntity)

    @Query("DELETE FROM tracks WHERE trackId = :trackId")
    suspend fun deleteFavoriteTrack(trackId: Int)

    @Query("SELECT * FROM tracks")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM tracks")
    suspend fun getFavoriteTracksId(): List<Int>
}