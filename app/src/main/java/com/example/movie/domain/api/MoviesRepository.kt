package com.example.movie.domain.api

import com.example.movie.data.dto.response.MovieDetailsResponse
import com.example.movie.domain.models.ActorCast
import com.example.movie.domain.models.Movie
import com.example.movie.domain.models.MovieCast
import com.example.movie.util.Resource
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun searchMovies(expression: String): Flow<Resource<List<Movie>>>
    fun getMovieDetails(id: String): Flow<Resource<MovieDetailsResponse>>
    fun getCastMovieInfo(id: String): Flow<Resource<MovieCast>>
    fun getCastActorInfo(actorName: String): Flow<Resource<List<ActorCast>>>
}