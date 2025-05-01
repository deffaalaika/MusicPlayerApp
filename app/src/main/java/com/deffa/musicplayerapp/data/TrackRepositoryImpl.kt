package com.deffa.musicplayerapp.data

import com.deffa.musicplayerapp.data.remote.SearchResponse
import com.deffa.musicplayerapp.domain.TrackRepository
import com.deffa.musicplayerapp.utils.Resource
import com.deffa.searchmodule.Track
import retrofit2.Response


class TrackRepositoryImpl(
    private val api: ItunesApiService = NetworkModule.itunesApiService
) : TrackRepository {

    override suspend fun searchTracks(term: String): Resource<List<Track>> {
        return try {
            val response: Response<SearchResponse> = api.searchTracks(term)

            if (response.isSuccessful) {
                val body: SearchResponse? = response.body()
                val list = body?.results.orEmpty()

                return if (list.isEmpty()) {
                    Resource.Empty
                } else {
                    Resource.Success(list, response.code())
                }

            } else {
                Resource.Error(
                    response.code(),
                    response.errorBody()?.string() ?: response.message()
                )
            }
        } catch (e: Exception) {
            Resource.Error(
                -1,
                e.localizedMessage ?: "Unknown error"
            )
        }
    }
}