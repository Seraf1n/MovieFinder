package ru.seraf1n.moviefinder.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.seraf1n.moviefinder.data.entity.TmdbResultsDTO

interface TmdbApi {
    @GET(ApiConstants.API_POPULAR)
    fun getFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbResultsDTO>
}