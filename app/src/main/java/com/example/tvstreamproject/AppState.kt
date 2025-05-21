package com.example.tvstreamproject

sealed class ScreenState {
    object Home : ScreenState()
    object Search : ScreenState()
    data class Detail(val seriesId: Int) : ScreenState()
}