package com.practicum.playlistmaker.domain.use_case.tracks_search

import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.consumer.ConsumerData
import com.practicum.playlistmaker.domain.model.Resource
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TracksRepository
import java.util.concurrent.Executors


class TracksSearchUseCase(private val repository: TracksRepository) {

    private val executor = Executors.newCachedThreadPool()

    fun execute(expression: String, consumer: Consumer<List<Track>>) {

        executor.execute {

            when (val tracksResponse = repository.searchTracks(expression)) {
                is Resource.Error -> {
                    consumer.consume(ConsumerData.Error(tracksResponse.message))
                }

                is Resource.Success -> {
                    consumer.consume(ConsumerData.Data(tracksResponse.data))
                }
            }

        }
    }
}