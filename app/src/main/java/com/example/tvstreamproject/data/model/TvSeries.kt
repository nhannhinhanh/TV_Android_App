package com.example.tvstreamproject.data.model

data class TvSeries(
    val id: Int,
    val name: String,
    val overview: String,
    val firstAirDate: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val originalLanguage: String,
    val originCountry: List<String>
)