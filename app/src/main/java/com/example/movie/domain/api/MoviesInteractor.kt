package com.example.movie.domain.api

import com.example.movie.data.dto.response.MovieDetailsResponse
import com.example.movie.domain.models.ActorCast
import com.example.movie.domain.models.Movie
import com.example.movie.domain.models.MovieCast

interface MoviesInteractor {
    fun searchMovies(expression: String, consumer: MoviesConsumer)
    interface MoviesConsumer {
        fun consume(foundMovies: List<Movie>?, errorMessage: String?)
    }

    fun getMovieInfo(id: String, consumer: MovieInfoConsumer)
    interface MovieInfoConsumer {
        fun consume(movieInfo: MovieDetailsResponse?, errorMessage: String?)
    }

    fun getCastInfo(id: String, consumer: MovieCastInfoConsumer)
    interface MovieCastInfoConsumer {
        fun consume(castMovieInfo: MovieCast?, errorMessage: String?)
    }

    fun getActorInfo(actorName: String, consumer: MoviesActorInfoConsumer)
    interface MoviesActorInfoConsumer {
        fun consume(castActorInfo: List<ActorCast>?, errorMessage: String?)
    }
}