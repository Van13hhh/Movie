package com.example.movie.ui.actor

import com.example.movie.domain.models.ActorCast

sealed interface ActorState {
    object Loading : ActorState

    data class Content(
        val actors: List<ActorCast>
    ) : ActorState

    data class Error(
        val errorMessage: String
    ) : ActorState

    data class Empty(
        val message: String
    ) : ActorState
}