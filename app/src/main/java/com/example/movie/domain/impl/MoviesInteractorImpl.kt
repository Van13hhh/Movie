package com.example.movie.domain.impl

import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.api.MoviesRepository

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    override fun searchMovies(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
        val t = Thread {
            consumer.consume(repository.searchMovies(expression))
        }
        t.start()
    }
}