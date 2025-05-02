package com.deffa.musicplayerapp

import com.deffa.musicplayerapp.data.remote.SearchResponse
import com.deffa.musicplayerapp.domain.SearchTrackUseCase
import com.deffa.musicplayerapp.domain.TrackRepository
import com.deffa.musicplayerapp.utils.Resource
import com.deffa.searchmodule.Track
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class SearchTrackUseCaseTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repo: TrackRepository
    private lateinit var useCase: SearchTrackUseCase
    private lateinit var lagu: String


    @Before
    fun setUp() {
        lagu = "lagu"
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        repo = mockk()
        useCase = SearchTrackUseCase(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `success onResult with data`() = runTest {
        //given
        val track = Track(1, "A", "B", "C", "D")
        val response = Response.success(SearchResponse(resultCount = 1, results = listOf(track)))
        coEvery { repo.searchTracks(lagu) } returns response
        var result: Resource<List<Track>>? = null
        //when
        useCase.execute(lagu) { resource ->
            result = resource
        }
        testDispatcher.scheduler.advanceUntilIdle()
        //then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success<List<Track>>).data.size)
        assertEquals(200, (result as Resource.Success<List<Track>>).code)
        assertEquals(listOf(track), (result as Resource.Success<List<Track>>).data)
    }


    @Test
    fun `Empty onResult with no Data`() = runTest {
        //given
        val response = Response.success(SearchResponse(resultCount = 0, results = emptyList()))
        coEvery { repo.searchTracks(lagu) } returns response
        var result: Resource<List<Track>>? = null

        //when
        useCase.execute(lagu) { resource ->
            result = resource
        }
        testDispatcher.scheduler.advanceUntilIdle()

        //then
        assertTrue(result is Resource.Empty)
        assertEquals(200, response.code())
    }

    @Test
    fun `execute emits Error when API returns HTTP error`() = runTest {
        // given
        val errorBody = "Service unavailable"
            .toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { repo.searchTracks(lagu) } returns Response.error(404, errorBody)

        var result: Resource<List<Track>>? = null

        // when
        useCase.execute(lagu) { resource ->
            result = resource
        }
        testDispatcher.scheduler.advanceUntilIdle()

        // then
        assertTrue(result is Resource.Error)
        result as Resource.Error
        assertEquals(404, (result as Resource.Error).code)
    }
}