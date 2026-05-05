package com.example.movie.presentation.movies

import com.example.movie.ui.movies.MoviesState

interface MoviesView {
    fun render(state: MoviesState)

    // One-time event methods
    fun showToast(additionalMessage: String)
}