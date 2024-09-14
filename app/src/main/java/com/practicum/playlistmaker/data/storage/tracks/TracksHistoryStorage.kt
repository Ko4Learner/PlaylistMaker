package com.practicum.playlistmaker.data.storage.tracks

import com.practicum.playlistmaker.data.dto.TrackStorageDto


interface TracksHistoryStorage {

    fun read(): MutableList<TrackStorageDto>

    fun clearHistory()

    fun addNewTrack(track: TrackStorageDto)

}