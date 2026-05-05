package com.example.movie

import android.app.Application
import com.example.movie.presentation.movies.MoviesSearchPresenter

class MoviesApplication : Application() {

    var moviesSearchPresenter: MoviesSearchPresenter? = null

}