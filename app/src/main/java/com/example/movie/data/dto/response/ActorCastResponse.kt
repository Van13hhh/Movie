package com.example.movie.data.dto.response

data class ActorCastResponse(
    val imageUrl: String,
    val nameActor: String,
    val description: String
) : Response()
