package com.deffa.musicplayerapp.data

import com.deffa.musicplayerapp.data.remote.SearchResponse
import com.deffa.musicplayerapp.domain.TrackRepository
import retrofit2.Response


class TrackRepositoryImpl(
    private val api: ItunesApiService
) : TrackRepository {
    override suspend fun searchTracks(term: String): Response<SearchResponse> =
        api.searchTracks(term)

}