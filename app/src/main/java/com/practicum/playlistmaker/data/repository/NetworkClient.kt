package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}