package com.deffa.musicplayerapp.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Track(
    @Json(name = "trackId") val id: Long,
    @Json(name = "trackName") val title: String,
    @Json(name = "artistName") val artist: String,
    @Json(name = "previewUrl") val previewUrl: String?,
    @Json(name = "artworkUrl100") val artworkUrl: String?
)
