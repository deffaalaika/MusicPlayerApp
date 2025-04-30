package com.deffa.musicplayerapp.data

import com.deffa.musicplayerapp.data.remote.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {

    @GET("search")
    suspend fun searchTracks(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("country") country: String = "ID",
        @Query("limit") limit: Int = 20
    ): Response<SearchResponse>
}