package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Resource
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import java.util.concurrent.Executors


class TracksSearchUseCase(private val repository: TracksRepository) {

    private val executor = Executors.newCachedThreadPool()

    operator fun invoke(expression: String, consumer: Consumer<List<Track>>) {

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