package com.example.tvstreamproject.data.model

data class TvDetail(
    val id: Int,
    val name: String,
    val originalName: String,
    val overview: String,
    val tagline: String,
    val status: String,
    val type: String,
    val popularity: Double,
    val voteAverage: Double,
    val voteCount: Int,
    val backdropPath: String?,
    val posterPath: String?,
    val firstAirDate: String,
    val lastAirDate: String?,
    val inProduction: Boolean,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val homepage: String,
    val originalLanguage: String,
    val originCountry: List<String>,
    val episodeRunTime: List<Int>,
    val genres: List<Genre>,
    val createdBy: List<Creator>,
    val lastEpisodeToAir: Episode?,
    val nextEpisodeToAir: Episode?,
    val networks: List<Network>,
    val productionCompanies: List<ProductionCompany>,
    val productionCountries: List<ProductionCountry>,
    val spokenLanguages: List<SpokenLanguage>,
    val seasons: List<Season>
)

data class Genre(
    val id: Int,
    val name: String
)

data class Creator(
    val id: Int,
    val creditId: String,
    val name: String,
    val originalName: String,
    val gender: Int,
    val profilePath: String?
)

data class Episode(
    val id: Int,
    val name: String,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int,
    val airDate: String,
    val episodeNumber: Int,
    val episodeType: String,
    val productionCode: String,
    val runtime: Int,
    val seasonNumber: Int,
    val showId: Int,
    val stillPath: String?
)

data class Network(
    val id: Int,
    val name: String,
    val originCountry: String,
    val logoPath: String?
)

data class ProductionCompany(
    val id: Int,
    val name: String,
    val originCountry: String,
    val logoPath: String?
)

data class ProductionCountry(
    val iso3166_1: String,
    val name: String
)

data class SpokenLanguage(
    val englishName: String,
    val iso639_1: String,
    val name: String
)

data class Season(
    val id: Int,
    val name: String,
    val overview: String,
    val seasonNumber: Int,
    val airDate: String?,
    val episodeCount: Int,
    val voteAverage: Double,
    val posterPath: String?
)
