package com.example.movie.data

import com.example.movie.data.dto.request.ActorCastRequest
import com.example.movie.data.dto.request.MovieCastRequest
import com.example.movie.data.dto.response.MovieDetailsResponse
import com.example.movie.data.dto.request.MovieInfoSearchRequest
import com.example.movie.data.dto.request.MoviesSearchRequest
import com.example.movie.data.dto.response.ActorCastResponse
import com.example.movie.data.dto.response.ActorResponse
import com.example.movie.data.dto.response.CastItemResponse
import com.example.movie.data.dto.response.DirectorsResponse
import com.example.movie.data.dto.response.MovieCastResponse
import com.example.movie.data.dto.response.MoviesSearchResponse
import com.example.movie.data.dto.response.WritersResponse
import com.example.movie.domain.api.MoviesRepository
import com.example.movie.domain.models.ActorCast
import com.example.movie.domain.models.Movie
import com.example.movie.domain.models.MovieCast
import com.example.movie.util.MovieCastConverter
import com.example.movie.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val converter: MovieCastConverter
) : MoviesRepository {
    override fun searchMovies(expression: String): Flow<Resource<List<Movie>>> = flow {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Success(getMockSearchResults()))
            }

            200 -> {
                if ((response as MoviesSearchResponse).results.isEmpty()) {
                    emit(Resource.Error("Empty error"))
                } else {
                    emit(Resource.Success((response).results.map {
                        Movie(it.id, it.resultType, it.image, it.title, it.description)
                    }))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun getMovieDetails(id: String): Flow<Resource<MovieDetailsResponse>> = flow{
        val response = networkClient.doRequest(MovieInfoSearchRequest(id))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Success(getMockMovieDetails()))
            }
            200 -> {
                if (response is MovieDetailsResponse) {
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Error("Empty server request"))
                }
            }

            else -> {
                emit(Resource.Error("Server Error"))
            }
        }
    }

    override fun getCastMovieInfo(id: String): Flow<Resource<MovieCast>> = flow{
        val response = networkClient.doRequest(MovieCastRequest(id))

         when (response.resultCode) {
            -1 -> {
                emit(Resource.Success(converter.convert(getMockCastResponse())))
            }

            200 -> {
                if (response is MovieCastResponse) {
                    emit(Resource.Success(converter.convert(response)))
                } else {
                    emit(Resource.Error("Empty server request"))
                }
            }

            else -> {
                emit(Resource.Error("Server Error"))
            }
        }
    }

    override fun getCastActorInfo(actorName: String): Flow<Resource<List<ActorCast>>> = flow {
        val response = networkClient.doRequest(ActorCastRequest(actorName))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Success(getMockCastActorInfo(actorName)))
            }

            200 -> {
                with(response is ActorCastResponse) {
                    emit(Resource.Success(getMockCastActorInfo(actorName)))
                }
            }

            else -> {
                emit(Resource.Error("Server Error"))
            }
        }
    }

    fun getMockCastActorInfo(actorName: String): List<ActorCast> {
        return listOf(
            ActorCast(
                imageUrl = "https://m.media-amazon.com/images/M/MV5BNzg1MTUyNDYxOF5BMl5BanBnXkFtZTgwNTQ4MTE2MjE@._V1_.jpg",
                nameActor = "Robert Downey Jr.",
                description = "American actor known for Iron Man role"
            ),
            ActorCast(
                imageUrl = "https://m.media-amazon.com/images/M/MV5BMTM3OTUwMDYwNl5BMl5BanBnXkFtZTcwNTUyNzc3Nw@@._V1_.jpg",
                nameActor = "Scarlett Johansson",
                description = "American actress known for Black Widow role"
            ),
            ActorCast(
                imageUrl = "https://m.media-amazon.com/images/M/MV5BMTQ2MjMwNDA3Nl5BMl5BanBnXkFtZTcwMTA2NDY3NQ@@._V1_.jpg",
                nameActor = "Tom Hanks",
                description = "American actor known for Forrest Gump"
            ),
            ActorCast(
                imageUrl = "", // Оставляем пустым, если нет фото
                nameActor = actorName,
                description = "Actor information not found"
            )
        )
    }
}

private fun getMockSearchResults(): List<Movie> {
    val allMovies = listOf(
        Movie(
            id = "tt0111161",
            resultType = "Movie",
            image = "https://m.media-amazon.com/images/M/MV5BMDAyY2FhYjctNDc5OS00MDNlLThiMGUtY2UxYWVkNGY2ZjljXkEyXkFqcGc@._V1_.jpg",
            title = "Побег из Шоушенка",
            description = "1994, Фрэнк Дарабонт"
        ),
        Movie(
            id = "tt0068646",
            resultType = "Movie",
            image = "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGc@._V1_.jpg",
            title = "Крёстный отец",
            description = "1972, Фрэнсис Форд Коппола"
        ),
        Movie(
            id = "tt0468569",
            resultType = "Movie",
            image = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg",
            title = "Тёмный рыцарь",
            description = "2008, Кристофер Нолан"
        ),
        Movie(
            id = "tt0109830",
            resultType = "Movie",
            image = "https://m.media-amazon.com/images/M/MV5BNWIwODRlZTUtY2U3ZS00Yzg1LWJhNzYtMmZiYmEyNmU1NjMzXkEyXkFqcGc@._V1_.jpg",
            title = "Форрест Гамп",
            description = "1994, Роберт Земекис"
        ),
        Movie(
            id = "tt0137523",
            resultType = "Movie",
            image = "https://m.media-amazon.com/images/M/MV5BNDYxNjQyMjAtNTdiOS00NGYwLWFmNTAtNThmYjU5ZGI2YTI1XkEyXkFqcGc@._V1_.jpg",
            title = "Бойцовский клуб",
            description = "1999, Дэвид Финчер"
        ),
        Movie(
            id = "tt1375666",
            resultType = "Movie",
            image = "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_.jpg",
            title = "Начало",
            description = "2010, Кристофер Нолан"
        ),
        Movie(
            id = "tt0167260",
            resultType = "Movie",
            image = "https://m.media-amazon.com/images/M/MV5BNzA5ZDNlZWMtM2NhNS00NDJjLTk4NDItYTRmY2EwMWZlZjY3XkEyXkFqcGc@._V1_.jpg",
            title = "Властелин колец: Возвращение короля",
            description = "2003, Питер Джексон"
        ),
        Movie(
            id = "tt0816692",
            resultType = "Movie",
            image = "https://m.media-amazon.com/images/M/MV5BZjdkOTU3MDktN2IxOS00OGEyLWFmMjktY2FiMmZkNWIyODZiXkEyXkFqcGc@._V1_.jpg",
            title = "Интерстеллар",
            description = "2014, Кристофер Нолан"
        )
    )
    return allMovies
}

private fun getMockMovieDetails(): MovieDetailsResponse {
    return MovieDetailsResponse(
        id = "000",
        title = "Побег из Шоушенка",
        imDbRating = "9.3",
        year = "1994",
        countries = "США",
        genres = "Драма",
        directors = "Фрэнк Дарабонт",
        writers = "Стивен Кинг (рассказ), Фрэнк Дарабонт (сценарий)",
        stars = "Тим Роббинс, Морган Фриман, Боб Гантон",
        plot = "Энди Дюфрейн, банкир, осуждён за убийство, которого он не совершал. Он попадает в тюрьму Шоушенк, где сталкивается с жестокостью и коррупцией. Благодаря своему уму и надежде он завоёвывает уважение заключённых и планирует невероятный побег."
    )
}


private fun getMockCastResponse(): MovieCastResponse {
    return MovieCastResponse(
        actors = listOf(
            ActorResponse(
                asCharacter = "Энди Дюфрейн",
                id = "nm0000209",
                image = "https://m.media-amazon.com/images/M/MV5BMTI1OTYxNzAxOF5BMl5BanBnXkFtZTYwNTE5ODI4._V1_.jpg",
                name = "Тим Роббинс"
            ),
            ActorResponse(
                asCharacter = "Эллис Бойд «Рэд» Реддинг",
                id = "nm0000151",
                image = "https://m.media-amazon.com/images/M/MV5BMTE5MTI4NjQ0Ml5BMl5BanBnXkFtZTYwNTk4NDE0._V1_.jpg",
                name = "Морган Фриман"
            ),
            ActorResponse(
                asCharacter = "Начальник тюрьмы Нортон",
                id = "nm0001303",
                image = "https://m.media-amazon.com/images/M/MV5BMTI2ODU0MzY5Ml5BMl5BanBnXkFtZTYwNTk4NDE0._V1_.jpg",
                name = "Боб Гантон"
            )
        ),
        directors = DirectorsResponse(
            items = listOf(
                CastItemResponse(
                    description = "",
                    id = "nm0001104",
                    name = "Фрэнк Дарабонт"
                )
            ),
            job = "Director"
        ),
        errorMessage = "",
        fullTitle = "Побег из Шоушенка (The Shawshank Redemption)",
        imDbId = "tt0111161",
        others = listOf(),
        title = "Побег из Шоушенка",
        type = "Movie",
        writers = WritersResponse(
            items = listOf(
                CastItemResponse(
                    description = "(based on the short story)",
                    id = "nm0001104",
                    name = "Стивен Кинг"
                ),
                CastItemResponse(
                    description = "(screenplay by)",
                    id = "nm0001104",
                    name = "Фрэнк Дарабонт"
                )
            ),
            job = "Writer"
        ),
        year = "1994"
    )
}