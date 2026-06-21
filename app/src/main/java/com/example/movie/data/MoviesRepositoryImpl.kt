package com.example.movie.data

import android.util.Log
import com.example.movie.data.dto.request.MovieCastRequest
import com.example.movie.data.dto.response.MovieDetailsResponse
import com.example.movie.data.dto.request.MovieInfoSearchRequest
import com.example.movie.data.dto.request.MoviesSearchRequest
import com.example.movie.data.dto.response.MovieCastResponse
import com.example.movie.data.dto.response.MoviesSearchResponse
import com.example.movie.domain.api.MoviesRepository
import com.example.movie.domain.models.Movie
import com.example.movie.domain.models.MovieCast
import com.example.movie.util.MovieCastConverter
import com.example.movie.util.Resource

class MoviesRepositoryImpl(private val networkClient: NetworkClient, private val converter: MovieCastConverter): MoviesRepository {
    override fun searchMovies(expression: String): Resource<List<Movie>> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        Log.d("DEBUG_REPO", "resultCode: ${response.resultCode}")
        Log.d("DEBUG_REPO", "response type: ${response::class.simpleName}")
        return when (response.resultCode) {
            -1 -> {
                Log.e("DEBUG_REPO", "Ошибка -1: нет интернета или таймаут")
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                if ((response as MoviesSearchResponse).results.isEmpty()){
                    Resource.Error("Empty error")
                }else {
                    Resource.Success((response).results.map {
                        Movie(it.id, it.resultType, it.image, it.title, it.description)
                    })
                }
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun getMovieInfo(id: String): Resource<MovieDetailsResponse> {
        Log.d("DEBUG_REPO", "1. getMovieInfo вызван с id: $id")

        val response = networkClient.doRequest(MovieInfoSearchRequest(id))
        Log.d("DEBUG_REPO", "2. Получен response, resultCode = ${response.resultCode}")
        Log.d("DEBUG_REPO", "3. response is MovieDetailsResponse? ${response is MovieDetailsResponse}")

        return when (response.resultCode) {
            -1 -> {
                Log.d("DEBUG_REPO", "4. Ошибка -1")
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Log.d("DEBUG_REPO", "5. Код 200")
                if (response is MovieDetailsResponse) {
                    Log.d("DEBUG_REPO", "6. Успех! Title: ${response.title}")
                    Resource.Success(response)
                } else {
                    Log.d("DEBUG_REPO", "7. Response не является MovieDetailsResponse")
                    Resource.Error("Empty server request")
                }
            }
            else -> {
                Log.d("DEBUG_REPO", "8. Другой код: ${response.resultCode}")
                Resource.Error("Server Error")
            }
        }
    }

    override fun getCastMovieInfo(id: String): Resource<MovieCast> {
        val response = networkClient.doRequest(MovieCastRequest(id))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                if (response is MovieCastResponse) {
                    Resource.Success(converter.convert(response))
                } else {
                    Resource.Error("Empty server request")
                }
            }
            else -> {
                Resource.Error("Server Error")
            }
        }
    }
}