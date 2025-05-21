package com.example.tvstreamproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvstreamproject.data.model.TvSeries
import com.example.tvstreamproject.data.repository.Result
import com.example.tvstreamproject.data.repository.TvRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: TvRepository = TvRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<TvSeries>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<TvSeries>>> = _uiState

    init {
        loadAiringToday()
    }

    private fun loadAiringToday() {
        viewModelScope.launch {
            when (val result = repository.getAiringToday()) {
                is Result.Success -> _uiState.value = UiState.Success(result.data)
                is Result.Error -> _uiState.value = UiState.Error(result.message)
            }
        }
    }
}
