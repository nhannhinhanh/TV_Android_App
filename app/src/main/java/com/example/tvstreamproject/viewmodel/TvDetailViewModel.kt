package com.example.tvstreamproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvstreamproject.data.model.TvDetail
import com.example.tvstreamproject.data.repository.Result
import com.example.tvstreamproject.data.repository.TvRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TvDetailViewModel(
    private val repository: TvRepository = TvRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<TvDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<TvDetail>> = _uiState

    fun loadTvDetail(seriesId: Int) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            when (val result = repository.getTvDetails(seriesId)) {
                is Result.Success -> _uiState.value = UiState.Success(result.data)
                is Result.Error -> _uiState.value = UiState.Error(result.message)
            }
        }
    }
}
