package com.example.movie.presentation.movies

import com.example.movie.domain.models.Movie

interface MoviesView {
    fun showPlaceholderMessage(isVisible: Boolean)
    fun showMoviesList(isVisible: Boolean)
    fun showProgressBar(isVisible: Boolean)
    fun changePlaceholderText(text: String)
    fun updateMovieList(newMoviesList: List<Movie>)
    fun showMessage(text: String)
}