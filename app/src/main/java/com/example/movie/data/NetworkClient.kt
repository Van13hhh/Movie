package com.example.movie.data

import com.example.movie.data.dto.response.Response

interface  NetworkClient {
    fun doRequest(dto: Any): Response
}