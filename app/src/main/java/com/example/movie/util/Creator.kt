package com.example.movie.util

import android.app.Activity
import android.content.Context
import com.example.movie.data.MoviesRepositoryImpl
import com.example.movie.data.network.RetrofitNetworkClient
import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.api.MoviesRepository
import com.example.movie.domain.impl.MoviesInteractorImpl
import com.example.movie.presentation.movies.MoviesSearchPresenter
import com.example.movie.presentation.poster.PosterPresenter
import com.example.movie.presentation.movies.MoviesView
import com.example.movie.presentation.poster.PosterView

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }
    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }
    fun provideMoviesSearchPresenter(moviesView: MoviesView, context: Context): MoviesSearchPresenter {
        return MoviesSearchPresenter(moviesView, context)
    }
    fun providePosterPresenter(view: PosterView, url: String): PosterPresenter{
        return PosterPresenter(view, url)
    }
}