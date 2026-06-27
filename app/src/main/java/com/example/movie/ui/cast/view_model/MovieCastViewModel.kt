package com.example.movie.ui.cast.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.models.MovieCast
import com.example.movie.ui.RVItem
import com.example.movie.ui.cast.MoviesCastRVItem

class MovieCastViewModel(
    movieId: String,
    moviesInteractor: MoviesInteractor,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<MoviesCastState>()
    fun observeState(): MutableLiveData<MoviesCastState> = stateLiveData

    init {
        stateLiveData.postValue(MoviesCastState.Loading)

        moviesInteractor.getCastInfo(movieId, object : MoviesInteractor.MovieCastInfoConsumer {
            override fun consume(
                castMovieInfo: MovieCast?,
                errorMessage: String?
            ) {
                if (castMovieInfo != null) {
                    stateLiveData.postValue(castToUiStateContent(castMovieInfo))
                } else {
                    stateLiveData.postValue(MoviesCastState.Error(errorMessage ?: "Unknown error"))
                }
            }

        })
    }

}

private fun castToUiStateContent(cast: MovieCast): MoviesCastState {
    val items = buildList<MoviesCastRVItem> {
        if (cast.directors.isNotEmpty()) {
            this += MoviesCastRVItem.HeaderItem("Directors")
            this += cast.directors.map { MoviesCastRVItem.PersonItem(it) }
        }

        if (cast.writers.isNotEmpty()) {
            this += MoviesCastRVItem.HeaderItem("Writers")
            this += cast.writers.map { MoviesCastRVItem.PersonItem(it) }
        }

        if (cast.actors.isNotEmpty()) {
            this += MoviesCastRVItem.HeaderItem("Actors")
            this += cast.actors.map { MoviesCastRVItem.PersonItem(it) }
        }

        if (cast.others.isNotEmpty()) {
            this += MoviesCastRVItem.HeaderItem("Others")
            this += cast.others.map { MoviesCastRVItem.PersonItem(it) }
        }
    }

    return MoviesCastState.Content(
        fullTitle = cast.fullTitle,
        items = items
    )

}

sealed interface MoviesCastState {

    object Loading : MoviesCastState

    data class Content(
        val fullTitle: String,
        val items: List<RVItem>
    ) : MoviesCastState

    data class Error(
        val message: String,
    ) : MoviesCastState

}