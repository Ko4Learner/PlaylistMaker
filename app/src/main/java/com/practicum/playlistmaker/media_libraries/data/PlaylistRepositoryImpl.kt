package com.practicum.playlistmaker.media_libraries.data

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat.getString
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media_libraries.data.db.PlaylistDatabase
import com.practicum.playlistmaker.media_libraries.data.db.PlaylistTracksDatabase
import com.practicum.playlistmaker.media_libraries.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_libraries.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media_libraries.data.db.mapper.PlaylistDbMapper
import com.practicum.playlistmaker.media_libraries.data.db.mapper.TrackDbMapper
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.media_libraries.domain.repository.PlaylistRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class PlaylistRepositoryImpl(
    private val playlistDatabase: PlaylistDatabase,
    private val playlistDbMapper: PlaylistDbMapper,
    private val playlistTracksDatabase: PlaylistTracksDatabase,
    private val trackDbMapper: TrackDbMapper,
    private val application: Application
) : PlaylistRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao()
            .insertPlaylist(playlistDbMapper.map(playlist))
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDatabase.playlistDao()
            .deletePlaylist(playlistId)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override fun getPlaylistTracks(tracksIdList: List<Int>): Flow<List<Track>> = flow {
        emit(convertFromTrackEntity(playlistTracksDatabase.playlistTracksDao().getAllPlaylistsTracks()).filter { track -> tracksIdList.contains(track.trackId) })
    }

    override suspend fun getPlaylist(playlistId: Int): Playlist {
        return playlistDbMapper.map(playlistDatabase.playlistDao().getPlaylist(playlistId))
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) {
        playlistDatabase.playlistDao()
            .updatePlaylist(playlistDbMapper.insertTrackMap(playlist,track))
        playlistTracksDatabase.playlistTracksDao().insertPlaylistTrack(trackDbMapper.map(track))
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri, playlistName: String): String {
        val contentResolver = application.applicationContext.contentResolver

        val filePath =
            File(
                application.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                getString(application.applicationContext,R.string.DirectoryImagePlaylist)
            )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(
            filePath,
            playlistName + Calendar.getInstance().time + getString(application.applicationContext,R.string.jpg)
        )

        val takeFlags: Int = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, takeFlags)

        val inputStream =
            application.applicationContext.contentResolver.openInputStream(uri)
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.path
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbMapper.map(playlist) }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbMapper.map(track) }
    }
}