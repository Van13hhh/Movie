package com.example.movie.data

import com.example.movie.data.dto.response.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}