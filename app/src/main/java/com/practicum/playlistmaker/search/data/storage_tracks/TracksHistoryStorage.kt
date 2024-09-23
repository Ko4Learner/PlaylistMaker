package com.practicum.playlistmaker.search.data.storage_tracks

import com.practicum.playlistmaker.search.data.dto.TrackStorageDto


interface TracksHistoryStorage {

    fun read(): MutableList<TrackStorageDto>

    fun clearHistory()

    fun addNewTrack(track: TrackStorageDto)

}