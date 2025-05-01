package com.deffa.musicplayerapp.presentation

import androidx.lifecycle.ViewModel
import com.deffa.musicplayerapp.data.TrackRepositoryImpl
import com.deffa.musicplayerapp.data.remote.Track
import com.deffa.musicplayerapp.domain.SearchTrackUseCase
import com.deffa.musicplayerapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    private val searchTrackUseCase: SearchTrackUseCase = SearchTrackUseCase(TrackRepositoryImpl())
) : ViewModel() {
    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    fun search(term: String) {
        _isLoading.value = true

        searchTrackUseCase.execute(term) { resource ->
            _isLoading.value = false

            when (resource) {
                is Resource.Success -> {

                    _tracks.value = resource.data
                }

                Resource.Empty -> {
                    _tracks.value = emptyList()

                }

                is Resource.Error -> {
                    _tracks.value = emptyList()
                    _errorMessage.value = "${resource.code} - ${resource.message}"

                }
            }
        }
    }
}