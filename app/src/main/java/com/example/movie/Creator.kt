package com.example.movie

import android.app.Activity
import android.content.Context
import com.example.movie.data.MoviesRepositoryImpl
import com.example.movie.data.network.RetrofitNetworkClient
import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.api.MoviesRepository
import com.example.movie.domain.impl.MoviesInteractorImpl
import com.example.movie.presentation.MoviesSearchController
import com.example.movie.ui.movies.MoviesAdapter

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }
    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }
    fun provideMoviesSearchController(activity: Activity, adapter: MoviesAdapter): MoviesSearchController {
        return MoviesSearchController(activity, adapter)
    }
}