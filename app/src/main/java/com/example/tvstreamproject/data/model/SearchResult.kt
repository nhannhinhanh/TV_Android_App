package com.example.tvstreamproject.data.model

data class SearchResult(
    val page: Int,
    val results: List<TvSeries>,
    val totalPages: Int,
    val totalResults: Int
)
