package com.practicum.playlistmaker.media_libraries.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media_libraries.data.db.entity.TrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks WHERE trackId = :trackId")
    suspend fun getPlaylistTrack(trackId: Int): TrackEntity

    @Query("SELECT * FROM tracks")
    suspend fun  getAllPlaylistsTracks(): List<TrackEntity>

    @Query("DELETE FROM tracks WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: Int)
}