package com.practicum.playlistmaker.iTunesApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TracksResponse>

}