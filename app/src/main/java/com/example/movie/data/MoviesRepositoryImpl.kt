package com.example.movie.data

import com.example.movie.data.dto.MoviesSearchRequest
import com.example.movie.data.dto.MoviesSearchResponse
import com.example.movie.domain.api.MoviesRepository
import com.example.movie.domain.models.Movie

class MoviesRepositoryImpl(private val networkClient: NetworkClient): MoviesRepository {
    override fun searchMovies(expression: String): List<Movie> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as MoviesSearchResponse).results.map {
                Movie(it.id, it.resultType, it.image, it.title, it.description) }
        } else {
            return emptyList()
        }
    }

}