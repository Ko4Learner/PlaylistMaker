package com.practicum.playlistmaker.search.data.storage_tracks

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.data.dto.TrackStorageDto

const val TRACK_LIST = "track_list"
const val TRACKS_SEARCH_HISTORY_SIZE = 10

class SharedPreferencesTracksStorage(private val sharedPreferences: SharedPreferences, private val gson: Gson) :
    TracksHistoryStorage {


    override fun read(): MutableList<TrackStorageDto> {
        val trackListType = object : TypeToken<MutableList<TrackStorageDto>>() {}.type
        val json = sharedPreferences.getString(TRACK_LIST, null) ?: return ArrayList()
        return gson.fromJson(json, trackListType)
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(TRACK_LIST).apply()
    }

    override fun addNewTrack(track: TrackStorageDto) {
        val historySize = TRACKS_SEARCH_HISTORY_SIZE
        val tracks = read()
        tracks.removeIf { it.trackId == track.trackId }
        if (tracks.size >= historySize) tracks.removeAt(historySize - 1)
        tracks.add(0, track)
        clearHistory()
        sharedPreferences.edit().putString(TRACK_LIST, gson.toJson(tracks)).apply()
    }
}