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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitNetworkClient(
    private val context: Context, private val imDbApiService: IMDbApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        return when (dto) {
            is MovieInfoSearchRequest -> {
                return withContext(Dispatchers.IO) {
                    try {
                        val response = imDbApiService.getMovieDetails(dto.id)
                        response.apply { resultCode = 200 }
                    } catch (_: SocketTimeoutException) {
                        Response().apply { resultCode = -1 }
                    } catch (_: UnknownHostException) {
                        Response().apply { resultCode = -1 }
                    } catch (_: Exception) {
                        Response().apply { resultCode = -1 }
                    }
                }
            }

            is MoviesSearchRequest -> {
                return withContext(Dispatchers.IO) {
                    try {
                        val response = imDbApiService.searchMovies(dto.expression)
                        response.apply { resultCode = 200 }
                    } catch (_: SocketTimeoutException) {
                        Response().apply { resultCode = -1 }
                    } catch (_: UnknownHostException) {
                        Response().apply { resultCode = -1 }
                    } catch (_: Exception) {
                        Response().apply { resultCode = -1 }
                    }
                }
            }

            is MovieCastRequest -> {
                return withContext(Dispatchers.IO) {
                    try {
                        val response = imDbApiService.getFullCast(dto.id)
                        response.apply { resultCode = 200 }
                    } catch (_: SocketTimeoutException) {
                        Response().apply { resultCode = -1 }
                    } catch (_: UnknownHostException) {
                        Response().apply { resultCode = -1 }
                    } catch (_: Exception) {
                        Response().apply { resultCode = -1 }
                    }
                }
            }

            is ActorCastRequest -> {
                return withContext(Dispatchers.IO) {
                    try {
                        val response = imDbApiService.getActorName(dto.actorName)
                        response.apply { resultCode = 200 }
                    } catch (_: SocketTimeoutException) {
                        Response().apply { resultCode = -1 }
                    } catch (_: UnknownHostException) {
                        Response().apply { resultCode = -1 }
                    } catch (_: Exception) {
                        Response().apply { resultCode = -1 }
                    }
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