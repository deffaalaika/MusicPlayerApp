package com.deffa.musicplayerapp

import com.deffa.musicplayerapp.data.ItunesApiService
import com.deffa.musicplayerapp.data.TrackRepositoryImpl
import com.deffa.musicplayerapp.data.remote.SearchResponse
import com.deffa.musicplayerapp.utils.Resource
import com.deffa.searchmodule.Track
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class TrackRepositoryImplTest {

    private val api = mockk<ItunesApiService>()
    private lateinit var repo: TrackRepositoryImpl

    @Before
    fun setUp() {
        repo = TrackRepositoryImpl(api)
    }

    @Test
    fun `searchTracks returns Empty when API returns zero results`() = runBlocking {
        // given
        val emptyResp = SearchResponse(resultCount = 0, results = emptyList())
        coEvery { api.searchTracks(term = "bitterlove") } returns Response.success(emptyResp)

        // when
        val result = repo.searchTracks("bitterlove")

        // then
        assertTrue(result is Resource.Empty)
    }

    @Test
    fun `searchTracks returns Success when API the list`() = runBlocking {
        // given
        val track = Track(id = 1, title = "T", artist = "A", previewUrl = "u", artworkUrl = null)
        val resp = SearchResponse(resultCount = 1, results = listOf(track))
        coEvery { api.searchTracks(term = "bitterlove") } returns Response.success(resp)

        // when
        val result = repo.searchTracks("bitterlove")

        // then
        assertTrue(result is Resource.Success)
        result as Resource.Success
        assertEquals(1, result.data.size)
        assertEquals(200, result.code)
    }
}