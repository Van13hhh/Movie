package com.example.movie.domain.api

import com.example.movie.data.dto.response.MovieDetailsResponse
import com.example.movie.domain.models.ActorCast
import com.example.movie.domain.models.Movie
import com.example.movie.domain.models.MovieCast
import kotlinx.coroutines.flow.Flow

interface MoviesInteractor {
    fun searchMovies(expression: String): Flow<Pair<List<Movie>?, String?>>
    fun getMovieInfo(id: String): Flow<Pair<MovieDetailsResponse?, String?>>
    fun getCastInfo(id: String): Flow<Pair<MovieCast?, String?>>
    fun getActorInfo(actorName: String): Flow<Pair<List<ActorCast>?, String?>>

}