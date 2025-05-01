package com.deffa.musicplayerapp.domain

import com.deffa.musicplayerapp.utils.Resource
import com.deffa.searchmodule.Track

interface TrackRepository {
    suspend fun searchTracks(term: String): Resource<List<Track>>
}