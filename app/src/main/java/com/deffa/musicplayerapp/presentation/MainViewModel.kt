package com.deffa.musicplayerapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deffa.musicplayerapp.domain.SearchTrackUseCase
import com.deffa.musicplayerapp.utils.Resource
import com.deffa.searchmodule.Track
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val searchTrackUseCase: SearchTrackUseCase
) : ViewModel() {
    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableSharedFlow<String>(replay = 0)
    val errorMessage: SharedFlow<String> = _errorMessage


    fun search(term: String) {
        viewModelScope.launch {
            _isLoading.value = true

            searchTrackUseCase.execute(term) { resource ->
                _isLoading.value = false

                when (resource) {
                    Resource.Empty -> {
                        _tracks.value = emptyList()
                    }

                    is Resource.Success -> {
                        _tracks.value = resource.data
                    }

                    is Resource.Error -> {
                        _tracks.value = emptyList()
                        viewModelScope.launch {
                            _errorMessage.emit("${resource.code} – ${resource.message}")
                        }
                    }
                }
            }
        }
    }
}