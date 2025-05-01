package com.deffa.musicplayerapp.domain

import com.deffa.musicplayerapp.data.remote.Track
import com.deffa.musicplayerapp.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchTrackUseCase(private val repo: TrackRepository) {
    fun execute(
        term: String,
        onResult: (Resource<List<Track>>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resource: Resource<List<Track>> = repo.searchTracks(term)
            withContext(Dispatchers.Main) {
                onResult(resource)
            }
        }
    }
}