package com.example.movie.domain.impl

import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.api.MoviesRepository
import com.example.movie.util.Resource
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchMovies(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
        executor.execute {
            when(val resource = repository.searchMovies(expression)){
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> consumer.consume(null, resource.message)
            }
        }
    }
}