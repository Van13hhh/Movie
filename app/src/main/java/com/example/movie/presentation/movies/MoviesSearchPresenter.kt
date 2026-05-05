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

class MoviesSearchPresenter(private val context: Context) {
    private val moviesInteractor = Creator.provideMoviesInteractor(context)
    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null
    private val movies = ArrayList<Movie>()
    private var view: MoviesView? = null
    private var state: MoviesState? = null
    private val latestSearchText: String? = null

    fun attachView(view: MoviesView){
        this.view = view
        state?.let { view.render(it) }
    }

    fun detachView(){
        this.view = null
    }

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.lastSearchText = changedText
        lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(MoviesState.Loading)

            moviesInteractor.searchMovies(newSearchText, object : MoviesInteractor.MoviesConsumer {
                @SuppressLint("NotifyDataSetChanged")
                override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                    handler.post {
                        if (foundMovies != null) {
                            movies.clear()
                            movies.addAll(foundMovies)
                        }
                        if (errorMessage != null) {
                            view?.showToast(errorMessage)
                            renderState(
                                MoviesState.Error(
                                    context.getString(R.string.something_went_wrong),
                                )
                            )
                        } else if (movies.isEmpty()) {
                            renderState(
                                MoviesState.Empty(
                                    context.getString(R.string.nothing_found),
                                )
                            )
                        } else {
                            renderState(
                                MoviesState.Content(
                                    movies,
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    private fun renderState(state: MoviesState) {
        this.state = state
        this.view?.render(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }
}