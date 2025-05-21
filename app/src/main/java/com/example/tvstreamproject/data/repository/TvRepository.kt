package com.example.tvstreamproject.data.repository

import com.example.tvstreamproject.data.api.TmdbClient
import com.example.tvstreamproject.data.model.TvDetail
import com.example.tvstreamproject.data.model.TvSeries

class TvRepository(private val client: TmdbClient = TmdbClient()) {

    suspend fun getAiringToday(): Result<List<TvSeries>> {
        return try {
            val tvList = client.getAiringTodayTVSeries()
            Result.Success(tvList)
        } catch (e: Exception) {
            Result.Error("Failed to fetch airing today TV shows", e)
        }
    }

    suspend fun searchTv(query: String): Result<List<TvSeries>> {
        return try {
            val result = client.searchTVSeries(query)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error("Failed to search TV series", e)
        }
    }

    suspend fun getTvDetails(seriesId: Int): Result<TvDetail> {
        return try {
            val detail = client.getTVSeriesDetails(seriesId)
            Result.Success(detail)
        } catch (e: Exception) {
            Result.Error("Failed to fetch TV series details", e)
        }
    }
}
