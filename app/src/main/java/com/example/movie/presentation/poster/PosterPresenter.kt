package com.example.movie.presentation.poster

class PosterPresenter(private val view: PosterView, private val url: String) {
    fun onCreate() {
        view.showPoster(url)
    }
}