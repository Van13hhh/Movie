package com.example.movie.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.movie.data.NetworkClient
import com.example.movie.data.dto.request.ActorCastRequest
import com.example.movie.data.dto.request.MovieCastRequest
import com.example.movie.data.dto.request.MovieInfoSearchRequest
import com.example.movie.data.dto.request.MoviesSearchRequest
import com.example.movie.data.dto.response.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitNetworkClient(
    private val context: Context, private val imDbApiService: IMDbApiService
) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        // 1. Проверка интернета
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        // 2. Обработка запросов через when (вместо цепочки if)
        return when (dto) {
            is MovieInfoSearchRequest -> {
                try {
                    val response = imDbApiService.getMovieDetails(dto.id).execute()
                    val body = response.body()

                    if (body != null) {
                        body.resultCode = response.code()
                        body
                    } else {
                        Response().apply { resultCode = response.code() }
                    }
                } catch (_: SocketTimeoutException) {
                    Response().apply { resultCode = -1 }
                } catch (_: UnknownHostException) {
                    Response().apply { resultCode = -1 }
                } catch (_: Exception) {
                    Response().apply { resultCode = -1 }
                }
            }

            is MoviesSearchRequest -> {
                try {
                    val response = imDbApiService.searchMovies(dto.expression).execute()
                    val body = response.body()

                    if (body != null) {
                        body.apply { resultCode = response.code() }
                    } else {
                        Response().apply { resultCode = response.code() }
                    }
                } catch (_: SocketTimeoutException) {
                    Response().apply { resultCode = -1 }
                } catch (_: UnknownHostException) {
                    Response().apply { resultCode = -1 }
                } catch (_: Exception) {
                    Response().apply { resultCode = -1 }
                }
            }

            is MovieCastRequest -> {
                try {
                    val response = imDbApiService.getFullCast(dto.id).execute()
                    val body = response.body()

                    if (body != null) {
                        body.resultCode = response.code()
                        body
                    } else {
                        Response().apply { resultCode = response.code() }
                    }
                } catch (_: SocketTimeoutException) {
                    Response().apply { resultCode = -1 }
                } catch (_: UnknownHostException) {
                    Response().apply { resultCode = -1 }
                } catch (_: Exception) {
                    Response().apply { resultCode = -1 }
                }
            }

            is ActorCastRequest -> {
                try {
                    val response = imDbApiService.getActorName(dto.actorName).execute()
                    val body = response.body()

                    if (body != null) {
                        body.resultCode = response.code()
                        body
                    } else {
                        Response().apply { resultCode = response.code() }
                    }
                } catch (_: SocketTimeoutException) {
                    Response().apply { resultCode = -1 }
                } catch (_: UnknownHostException) {
                    Response().apply { resultCode = -1 }
                } catch (_: Exception) {
                    Response().apply { resultCode = -1 }
                }
            }


            else -> {
                Response().apply { resultCode = 400 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_WIFI
        ) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}