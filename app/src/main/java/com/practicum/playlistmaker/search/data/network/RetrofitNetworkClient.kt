package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest

class RetrofitNetworkClient(private val iTunesApi: iTunesApi) : NetworkClient {

    override fun doRequest(dto: Any): Response {

        return try {
            val resp = iTunesApi.search((dto as TracksSearchRequest).expression).execute()
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        } catch (ex: Exception) {
            Response().apply { resultCode = 400 }
        }

    }
}