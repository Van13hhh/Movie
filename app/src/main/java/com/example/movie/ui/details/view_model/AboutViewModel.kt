package com.example.movie.ui.details.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.data.dto.response.MovieDetailsResponse
import com.example.movie.domain.api.MoviesInteractor
import kotlinx.coroutines.launch

class AboutViewModel(
    movieId: String,
    moviesInteractor: MoviesInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<MoviesInfoState>()
    fun observeState(): LiveData<MoviesInfoState> = stateLiveData

    init {
        viewModelScope.launch {
            moviesInteractor.getMovieInfo(movieId)
                .collect { pair ->
                    if (pair.second != null) {
                        stateLiveData.postValue(MoviesInfoState.Error(pair.second ?: "Unknown Error"))
                    }else if (pair.first == null) {
                        stateLiveData.postValue(MoviesInfoState.Error("Empty Error"))
                    } else {
                        stateLiveData.postValue(MoviesInfoState.Content((pair.first!!)))
                    }
                }
        }
    }

    sealed interface MoviesInfoState {
        data class Content(
            val movie: MovieDetailsResponse
        ) : MoviesInfoState

        data class Error(
            val errorMessage: String
        ) : MoviesInfoState
    }

}