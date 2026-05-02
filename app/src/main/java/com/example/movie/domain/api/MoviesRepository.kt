package com.example.movie.domain.api

import com.example.movie.domain.models.Movie

interface MoviesRepository {
    fun searchMovies(expression: String): List<Movie>
}