package com.deffa.musicplayerapp.domain

import com.deffa.musicplayerapp.data.remote.SearchResponse
import com.deffa.musicplayerapp.utils.Resource
import com.deffa.searchmodule.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

class SearchTrackUseCase(private val repo: TrackRepository) {
    fun execute(
        term: String,
        onResult: (Resource<List<Track>>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resource = try {

                val response: Response<SearchResponse> = repo.searchTracks(term)
                if (response.isSuccessful) {
                    val tracks = response.body()?.results.orEmpty()
                    if (tracks.isEmpty()) {
                        Resource.Empty
                    } else {
                        Resource.Success(data = tracks, code = response.code())
                    }

                } else {
                    Resource.Error(code = response.code(), message = response.message())
                }

            } catch (e: HttpException) {
                Resource.Error(code = e.code(), message = e.message())
            } catch (e: Exception) {
                Resource.Error(code = -1, message = e.localizedMessage ?: "Unknown Error")
            }

            withContext(Dispatchers.Main) {
                onResult(resource)
            }
        }
    }
}