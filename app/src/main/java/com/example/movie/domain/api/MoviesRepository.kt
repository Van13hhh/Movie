package com.example.movie.domain.api

import com.example.movie.domain.models.Movie
import com.example.movie.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
}