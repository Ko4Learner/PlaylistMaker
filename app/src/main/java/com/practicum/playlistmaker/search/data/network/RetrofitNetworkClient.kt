package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(iTunesApi::class.java)


    override fun doRequest(dto: Any): Response {

        return try {
            val resp = imdbService.search((dto as TracksSearchRequest).expression).execute()
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        } catch (ex: Exception) {
            Response().apply { resultCode = 400 }
        }

    }
}