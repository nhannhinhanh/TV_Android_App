package com.example.tvstreamproject.utils

object ImageUtil {

    private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"

    fun buildImageUrl(path: String?, size: String = "original"): String? {
        return if (!path.isNullOrBlank()) {
            "$TMDB_IMAGE_BASE_URL$size$path"
        } else {
            null
        }
    }
}
