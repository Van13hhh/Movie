package com.example.movie.ui.movies.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.models.Movie
import com.example.movie.ui.movies.MoviesState
import com.example.movie.util.debounce
import kotlinx.coroutines.launch

class MoviesViewModel(private val moviesInteractor: MoviesInteractor, val errorMessage: String) :
    ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

    }

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private var latestSearchText: String? = null
    private val movieSearchDebounce : (String) -> Unit = debounce(SEARCH_DEBOUNCE_DELAY, viewModelScope, true){ text ->
        searchRequest(text)
    }

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            this.latestSearchText = changedText
            movieSearchDebounce (changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                MoviesState.Loading
            )

            val movies = mutableListOf<Movie>()

            viewModelScope.launch {
                moviesInteractor
                    .searchMovies(newSearchText)
                    .collect { pair ->
                        if (pair.first != null) {
                            movies.addAll(pair.first!!)
                        }

                        when {
                            pair.second != null -> {
                                renderState(
                                    MoviesState.Error(
                                        errorMessage = pair.second!!
                                    )
                                )

                                showToast.postValue(pair.second)

                            }

                            movies.isEmpty() -> {
                                renderState(
                                    MoviesState.Error(
                                        errorMessage = "Empty"
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    MoviesState.Content(
                                        movies = movies,
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
}