package com.example.movie.domain.impl

import com.example.movie.data.dto.response.MovieDetailsResponse
import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.api.MoviesRepository
import com.example.movie.domain.models.ActorCast
import com.example.movie.domain.models.Movie
import com.example.movie.domain.models.MovieCast
import com.example.movie.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    override fun searchMovies(expression: String):  Flow<Pair<List<Movie>?, String?>> {
        return repository.searchMovies(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getMovieInfo(id: String):Flow<Pair<MovieDetailsResponse?, String?>> {
        return repository.getMovieDetails(id).map { result ->
            when(result){
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getCastInfo(
        id: String
    ): Flow<Pair<MovieCast?, String?>> {
        return  repository.getCastMovieInfo(id).map { result ->
            when(result){
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getActorInfo(actorName: String): Flow<Pair<List<ActorCast>?, String?>> {
        return repository.getCastActorInfo(actorName).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}