package com.example.movie.data

import com.example.movie.data.dto.MoviesSearchRequest
import com.example.movie.data.dto.MoviesSearchResponse
import com.example.movie.domain.api.MoviesRepository
import com.example.movie.domain.models.Movie
import com.example.movie.util.Resource

class MoviesRepositoryImpl(private val networkClient: NetworkClient): MoviesRepository {
    override fun searchMovies(expression: String): Resource<List<Movie>> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                if ((response as MoviesSearchResponse).results.isEmpty()){
                    Resource.Error("сасамбаев")
                }else {
                    Resource.Success((response as MoviesSearchResponse).results.map {
                        Movie(it.id, it.resultType, it.image, it.title, it.description)
                    })
                }
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}