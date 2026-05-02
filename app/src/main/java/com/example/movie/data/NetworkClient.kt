package com.example.movie.data

import com.example.movie.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}