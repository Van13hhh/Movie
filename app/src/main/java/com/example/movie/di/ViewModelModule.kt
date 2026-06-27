package com.example.movie.di

import com.example.movie.R
import com.example.movie.ui.actor.view_model.ActorViewModel
import com.example.movie.ui.cast.view_model.MovieCastViewModel
import com.example.movie.ui.movies.view_model.MoviesViewModel
import com.example.movie.ui.details.view_model.PosterViewModel
import com.example.movie.ui.details.view_model.AboutViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MoviesViewModel(get(), androidContext().getString(R.string.something_went_wrong))
    }

    viewModel { (posterUrl: String) ->
        PosterViewModel(posterUrl)
    }

    viewModel { (movieId: String) ->
        AboutViewModel(movieId, get())
    }

    viewModel { (movieId: String) ->
        MovieCastViewModel(movieId, get())
    }

    viewModel { ActorViewModel(get(), androidContext().getString(R.string.nothing_found)) }

}