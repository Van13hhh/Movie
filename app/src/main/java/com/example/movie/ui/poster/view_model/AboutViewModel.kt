package com.example.movie.ui.poster.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.data.dto.response.MovieDetailsResponse
import com.example.movie.domain.api.MoviesInteractor

class AboutViewModel(private val movieId: String,
                     private val moviesInteractor: MoviesInteractor, ) : ViewModel() {

    private val stateLiveData = MutableLiveData<MoviesInfoState>()
    fun observeState(): LiveData<MoviesInfoState> = stateLiveData

    init {
        moviesInteractor.getMovieInfo(movieId, object : MoviesInteractor.MovieInfoConsumer{
            override fun consume(
                movieInfo: MovieDetailsResponse?,
                errorMessage: String?
            ) {
                if (errorMessage != null){
                    stateLiveData.postValue(MoviesInfoState.Error("Error"))
                }else if (movieInfo == null){
                    stateLiveData.postValue(MoviesInfoState.Error("Empty Error"))
                }else{
                    stateLiveData.postValue(MoviesInfoState.Content(movieInfo))
                }
            }

        })
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