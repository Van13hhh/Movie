package com.example.movie.domain.api

import com.example.movie.data.dto.response.MovieDetailsResponse
import com.example.movie.domain.models.Movie
import com.example.movie.domain.models.MovieCast
import com.example.movie.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
    fun getMovieDetails(id: String): Resource<MovieDetailsResponse>
    fun getCastMovieInfo(id: String): Resource<MovieCast>
}