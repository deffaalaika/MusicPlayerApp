package com.deffa.musicplayerapp.domain

import com.deffa.musicplayerapp.data.remote.Track
import com.deffa.musicplayerapp.utils.Resource

interface TrackRepository {
    suspend fun searchTracks(term: String): Resource<List<Track>>
}