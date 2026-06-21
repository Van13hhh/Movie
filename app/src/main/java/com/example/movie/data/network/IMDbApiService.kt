package com.example.movie.data.network

import com.example.movie.data.dto.response.MovieCastResponse
import com.example.movie.data.dto.response.MovieDetailsResponse
import com.example.movie.data.dto.response.MoviesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IMDbApiService {
    @GET("en/API/SearchMovie/k_zcuw1ytf/{expression}")
    fun searchMovies(@Path("expression") expression: String): Call<MoviesSearchResponse>

    @GET("en/API/Title/k_zcuw1ytf/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: String): Call<MovieDetailsResponse>

    @GET("en/API/FullCast/k_zcuw1ytf/{movie_id}")
    fun getFullCast(@Path("movie_id") movieId: String): Call<MovieCastResponse>
}