package ru.seraf1n.remote_module.entity

import com.google.gson.annotations.SerializedName

class TmdbResultsDTO (
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val tmdbFilms: List<TmdbFilm>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)