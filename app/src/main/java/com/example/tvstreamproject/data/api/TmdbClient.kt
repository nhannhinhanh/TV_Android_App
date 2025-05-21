package com.example.tvstreamproject.data.api

import com.example.tvstreamproject.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import org.json.JSONObject

class TmdbClient {

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val API_BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYzZhMzNjNjIxMDhlNzc1YWYxYjQ5NTBhM2Q2MjQzZCIsIm5iZiI6MTc0NzU2MDYyOC43NTEwMDAyLCJzdWIiOiI2ODI5YThiNGNjMWQ2NTc3N2Q4YzA0ODgiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.1JDCgSWsioI6YZwjG1M18tL5Qw8vw1HXXYcgWbBcVXY" // truncated for brevity
    }

    private val client = OkHttpClient()

    suspend fun getAiringTodayTVSeries(): List<TvSeries> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("${BASE_URL}tv/airing_today?language=en-US&page=1")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", API_BEARER_TOKEN)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }

        val body = response.body?.string() ?: throw IOException("Empty response body")
        val json = JSONObject(body)
        val results = json.getJSONArray("results")

        val seriesList = mutableListOf<TvSeries>()
        for (i in 0 until results.length()) {
            val item = results.getJSONObject(i)
            seriesList.add(
                TvSeries(
                    id = item.getInt("id"),
                    name = item.getString("name"),
                    overview = item.getString("overview"),
                    firstAirDate = item.optString("first_air_date", ""),
                    posterPath = item.optString("poster_path", null),
                    backdropPath = item.optString("backdrop_path", null),
                    voteAverage = item.optDouble("vote_average", 0.0),
                    voteCount = item.optInt("vote_count", 0),
                    popularity = item.optDouble("popularity", 0.0),
                    originalLanguage = item.optString("original_language", ""),
                    originCountry = item.optJSONArray("origin_country")?.let { array ->
                        List(array.length()) { index -> array.getString(index) }
                    } ?: emptyList()
                )
            )
        }

        return@withContext seriesList
    }

    suspend fun getTVSeriesDetails(seriesId: Int): TvDetail = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("${BASE_URL}tv/$seriesId?language=en-US")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", API_BEARER_TOKEN)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw IOException("Failed to fetch TV series details: $response")
        }

        val body = response.body?.string() ?: throw IOException("Empty response body")
        val json = JSONObject(body)

        return@withContext TvDetail(
            id = json.getInt("id"),
            name = json.getString("name"),
            originalName = json.getString("original_name"),
            overview = json.optString("overview", ""),
            tagline = json.optString("tagline", ""),
            status = json.optString("status", ""),
            type = json.optString("type", ""),
            popularity = json.optDouble("popularity", 0.0),
            voteAverage = json.optDouble("vote_average", 0.0),
            voteCount = json.optInt("vote_count", 0),
            backdropPath = json.optString("backdrop_path", null),
            posterPath = json.optString("poster_path", null),
            firstAirDate = json.optString("first_air_date", ""),
            lastAirDate = json.optString("last_air_date", ""),
            inProduction = json.optBoolean("in_production", false),
            numberOfEpisodes = json.optInt("number_of_episodes", 0),
            numberOfSeasons = json.optInt("number_of_seasons", 0),
            homepage = json.optString("homepage", ""),
            originalLanguage = json.optString("original_language", ""),
            originCountry = json.optJSONArray("origin_country")?.let {
                List(it.length()) { i -> it.getString(i) }
            } ?: emptyList(),
            episodeRunTime = json.optJSONArray("episode_run_time")?.let {
                List(it.length()) { i -> it.getInt(i) }
            } ?: emptyList(),
            genres = json.getJSONArray("genres").let { array ->
                List(array.length()) { i ->
                    val obj = array.getJSONObject(i)
                    Genre(obj.getInt("id"), obj.getString("name"))
                }
            },
            createdBy = json.getJSONArray("created_by").let { array ->
                List(array.length()) { i ->
                    val obj = array.getJSONObject(i)
                    Creator(
                        id = obj.getInt("id"),
                        creditId = obj.getString("credit_id"),
                        name = obj.getString("name"),
                        originalName = obj.getString("original_name"),
                        gender = obj.optInt("gender", 0),
                        profilePath = obj.optString("profile_path", null)
                    )
                }
            },
            lastEpisodeToAir = json.optJSONObject("last_episode_to_air")?.let { obj ->
                Episode(
                    id = obj.getInt("id"),
                    name = obj.getString("name"),
                    overview = obj.optString("overview", ""),
                    voteAverage = obj.optDouble("vote_average", 0.0),
                    voteCount = obj.optInt("vote_count", 0),
                    airDate = obj.optString("air_date", ""),
                    episodeNumber = obj.optInt("episode_number", 0),
                    episodeType = obj.optString("episode_type", ""),
                    productionCode = obj.optString("production_code", ""),
                    runtime = obj.optInt("runtime", 0),
                    seasonNumber = obj.optInt("season_number", 0),
                    showId = obj.optInt("show_id", 0),
                    stillPath = obj.optString("still_path", null)
                )
            },
            nextEpisodeToAir = null, // you can add similar block for "next_episode_to_air" if needed
            networks = json.getJSONArray("networks").let { array ->
                List(array.length()) { i ->
                    val obj = array.getJSONObject(i)
                    Network(
                        id = obj.getInt("id"),
                        name = obj.getString("name"),
                        originCountry = obj.optString("origin_country", ""),
                        logoPath = obj.optString("logo_path", null)
                    )
                }
            },
            productionCompanies = json.getJSONArray("production_companies").let { array ->
                List(array.length()) { i ->
                    val obj = array.getJSONObject(i)
                    ProductionCompany(
                        id = obj.getInt("id"),
                        name = obj.getString("name"),
                        originCountry = obj.optString("origin_country", ""),
                        logoPath = obj.optString("logo_path", null)
                    )
                }
            },
            productionCountries = json.getJSONArray("production_countries").let { array ->
                List(array.length()) { i ->
                    val obj = array.getJSONObject(i)
                    ProductionCountry(
                        iso3166_1 = obj.getString("iso_3166_1"),
                        name = obj.getString("name")
                    )
                }
            },
            spokenLanguages = json.getJSONArray("spoken_languages").let { array ->
                List(array.length()) { i ->
                    val obj = array.getJSONObject(i)
                    SpokenLanguage(
                        englishName = obj.getString("english_name"),
                        iso639_1 = obj.getString("iso_639_1"),
                        name = obj.optString("name", "")
                    )
                }
            },
            seasons = json.getJSONArray("seasons").let { array ->
                List(array.length()) { i ->
                    val obj = array.getJSONObject(i)
                    Season(
                        id = obj.getInt("id"),
                        name = obj.getString("name"),
                        overview = obj.optString("overview", ""),
                        seasonNumber = obj.getInt("season_number"),
                        airDate = obj.optString("air_date", null),
                        episodeCount = obj.optInt("episode_count", 0),
                        voteAverage = obj.optDouble("vote_average", 0.0),
                        posterPath = obj.optString("poster_path", null)
                    )
                }
            }
        )
    }

    suspend fun searchTVSeries(query: String): List<TvSeries> = withContext(Dispatchers.IO) {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addPathSegment("search")
            .addPathSegment("tv")
            .addQueryParameter("language", "en-US")
            .addQueryParameter("page", "1")
            .addQueryParameter("query", query)
            .build()

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", API_BEARER_TOKEN)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw IOException("Search failed: $response")
        }

        val body = response.body?.string() ?: throw IOException("Empty response body")
        val json = JSONObject(body)
        val results = json.getJSONArray("results")

        val searchResults = mutableListOf<TvSeries>()
        for (i in 0 until results.length()) {
            val item = results.getJSONObject(i)
            searchResults.add(
                TvSeries(
                    id = item.getInt("id"),
                    name = item.getString("name"),
                    overview = item.optString("overview", ""),
                    firstAirDate = item.optString("first_air_date", ""),
                    posterPath = item.optString("poster_path", null),
                    backdropPath = item.optString("backdrop_path", null),
                    voteAverage = item.optDouble("vote_average", 0.0),
                    voteCount = item.optInt("vote_count", 0),
                    popularity = item.optDouble("popularity", 0.0),
                    originalLanguage = item.optString("original_language", ""),
                    originCountry = item.optJSONArray("origin_country")?.let { array ->
                        List(array.length()) { idx -> array.getString(idx) }
                    } ?: emptyList()
                )
            )
        }

        return@withContext searchResults
    }


}