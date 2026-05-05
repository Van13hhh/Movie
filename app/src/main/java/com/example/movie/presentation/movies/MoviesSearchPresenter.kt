package com.example.movie.presentation.movies

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.movie.R
import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.models.Movie
import com.example.movie.ui.movies.MoviesState
import com.example.movie.util.Creator

class MoviesSearchPresenter(private val view: MoviesView, private val context: Context) {
    private val moviesInteractor = Creator.provideMoviesInteractor(context)
    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null
    private val movies = ArrayList<Movie>()

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText)
    }

    fun searchDebounce(changedText: String) {
        lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            view.render(
                    MoviesState.Loading
            )

            moviesInteractor.searchMovies(newSearchText, object : MoviesInteractor.MoviesConsumer {
                @SuppressLint("NotifyDataSetChanged")
                override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                    handler.post {
                        if (foundMovies != null) {
                            movies.clear()
                            movies.addAll(foundMovies)
                        }
                        if (errorMessage != null) {
                            view.showToast(errorMessage)
                            view.render(MoviesState.Error(
                                context.getString(R.string.something_went_wrong)
                            ))
                        } else if (movies.isEmpty()) {
                            view.render(
                                MoviesState.Empty(
                                    context.getString(R.string.something_went_wrong)
                                )
                            )
                        } else {
                            view.render(MoviesState.Content(
                                movies
                            ))
                        }
                    }
                }
            })
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }
}