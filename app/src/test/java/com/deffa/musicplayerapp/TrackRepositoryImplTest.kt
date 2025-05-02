package com.deffa.musicplayerapp

import com.deffa.musicplayerapp.data.ItunesApiService
import com.deffa.musicplayerapp.data.TrackRepositoryImpl
import com.deffa.musicplayerapp.data.remote.SearchResponse
import com.deffa.searchmodule.Track
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class TrackRepositoryImplTest {

    private lateinit var api: ItunesApiService
    private lateinit var repo: TrackRepositoryImpl
    private lateinit var lagu: String

    @Before
    fun setUp() {
        lagu = "lagu"
        api = mockk()
        repo = TrackRepositoryImpl(api)
    }


    @Test
    fun `success api return response`() = runTest {
        //given
        val track = Track(1, "A", "B", "C", "D")
        val searchResponse = SearchResponse(resultCount = 1, results = listOf(track))
        val success = Response.success(searchResponse)
        coEvery { api.searchTracks(lagu) } returns success

        //when
        val result = repo.searchTracks(lagu)

        //then
        assertTrue(result.isSuccessful)
        assertEquals(200, result.code())
        assertEquals(searchResponse, result.body())

    }

    @Test
    fun `success api return empty`() = runTest {
        //given
        val searchResponse = SearchResponse(resultCount = 0, results = emptyList())
        val success = Response.success(searchResponse)
        coEvery { api.searchTracks(lagu) } returns success

        //when
        val result = repo.searchTracks(lagu)

        //then
        assertTrue(result.isSuccessful)
        assertEquals(200, result.code())
        assertEquals(searchResponse.results, result.body()?.results)
    }

    @Test
    fun `Error Response Api`() = runTest {
        //given
        val errorBody = "404 Error".toResponseBody("text/plain".toMediaTypeOrNull())
        val errorResponse: Response<SearchResponse> = Response.error(404, errorBody)
        coEvery { api.searchTracks(lagu) } returns errorResponse

        //when
        val result = repo.searchTracks(lagu)

        //then
        assertFalse(result.isSuccessful)
        assertEquals(404, result.code())
        assertEquals(null, result.body())
    }

}