package com.example.movie.ui.actor.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.models.ActorCast
import com.example.movie.ui.actor.ActorState

class ActorViewModel(private val moviesInteractor: MoviesInteractor, val errorMessage: String) :
    ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

    }

    private val stateLiveData = MutableLiveData<ActorState>()
    fun observeState(): LiveData<ActorState> = stateLiveData

    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                ActorState.Loading
            )

            moviesInteractor.getActorInfo(
                newSearchText,
                object : MoviesInteractor.MoviesActorInfoConsumer {
                    override fun consume(
                        castActorInfo: List<ActorCast>?,
                        errorMessage: String?
                    ) {
                        handler.post {
                            // Готовим список найденных фильмов для передачи в конструктор MoviesState
                            val actors = mutableListOf<ActorCast>()
                            if (castActorInfo != null) {
                                actors.addAll(castActorInfo)
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
                    }
                })
        }
    }

    private fun renderState(state: ActorState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
}