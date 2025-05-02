package com.deffa.musicplayerapp.domain

import com.deffa.musicplayerapp.data.remote.SearchResponse
import retrofit2.Response

interface TrackRepository {
    suspend fun searchTracks(term: String): Response<SearchResponse>
}