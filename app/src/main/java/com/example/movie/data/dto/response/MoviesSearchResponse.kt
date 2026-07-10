package com.example.movie.data.dto.response

import com.example.movie.data.dto.MovieDto

data class MoviesSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<MovieDto>
) : Response()