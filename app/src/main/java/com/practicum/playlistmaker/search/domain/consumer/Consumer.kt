package com.practicum.playlistmaker.search.domain.consumer

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}