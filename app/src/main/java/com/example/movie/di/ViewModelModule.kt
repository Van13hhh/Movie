package com.example.movie.di

import com.example.movie.ui.cast.view_model.MovieCastViewModel
import com.example.movie.ui.movies.view_model.MoviesViewModel
import com.example.movie.ui.poster.view_model.PosterViewModel
import com.example.movie.ui.poster.view_model.AboutViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MoviesViewModel(get(), androidContext())
    }

    viewModel {(posterUrl: String) ->
        PosterViewModel(posterUrl)
    }

    viewModel {(movieId: String) ->
        AboutViewModel(movieId, get())
    }

    viewModel {(movieId: String) ->
        MovieCastViewModel(movieId, get())
    }

}