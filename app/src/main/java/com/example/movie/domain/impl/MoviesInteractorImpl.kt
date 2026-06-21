package com.example.movie.domain.impl

import android.util.Log
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

    override fun getMovieInfo(id: String, consumer: MoviesInteractor.MovieInfoConsumer) {
        Log.d("DEBUG_INTERACTOR", "getMovieInfo вызван с id: $id")

        executor.execute {
            Log.d("DEBUG_INTERACTOR", "Запрос в репозиторий...")
            when(val resource = repository.getMovieInfo(id)){
                is Resource.Success -> {
                    Log.d("DEBUG_INTERACTOR", "Успех: ${resource.data?.title}")
                    consumer.consume(resource.data, null)
                }
                is Resource.Error -> {
                    Log.d("DEBUG_INTERACTOR", "Ошибка: ${resource.message}")
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun getCastInfo(
        id: String,
        consumer: MoviesInteractor.MovieCastInfoConsumer
    ) {
        executor.execute {
            when(val resource = repository.getCastMovieInfo(id)){
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }
                is Resource.Error -> {
                    consumer.consume(resource.data, resource.message)
                }
            }
        }
    }
}