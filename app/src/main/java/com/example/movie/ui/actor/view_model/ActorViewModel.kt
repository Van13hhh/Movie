package com.example.movie.ui.actor.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.models.ActorCast
import com.example.movie.ui.actor.ActorState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class ActorViewModel(private val moviesInteractor: MoviesInteractor, val errorMessage: String) :
    ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val stateLiveData = MutableLiveData<ActorState>()
    fun observeState(): LiveData<ActorState> = stateLiveData

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY.milliseconds)
            searchRequest(changedText)
        }

    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                ActorState.Loading
            )

            viewModelScope.launch {
                moviesInteractor
                    .getActorInfo(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundNames: List<ActorCast>?, errorMessage: String?) {
        val actors = mutableListOf<ActorCast>()
        if (foundNames != null) {
            actors.addAll(foundNames)
        }

        when {
            errorMessage != null -> {
                renderState(
                    ActorState.Error(
                        errorMessage = errorMessage
                    )
                )
            }

            actors.isEmpty() -> {
                renderState(
                    ActorState.Error(
                        errorMessage = "Empty"
                    )
                )
            }

            else -> {
                renderState(
                    ActorState.Content(
                        actors = actors,
                    )
                )
            }
        }
    }

    private fun renderState(state: ActorState) {
        stateLiveData.postValue(state)
    }
}