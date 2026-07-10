package com.example.movie.util

import com.example.movie.data.dto.response.ActorResponse
import com.example.movie.data.dto.response.CastItemResponse
import com.example.movie.data.dto.response.DirectorsResponse
import com.example.movie.data.dto.response.MovieCastResponse
import com.example.movie.data.dto.response.OtherResponse
import com.example.movie.data.dto.response.WritersResponse
import com.example.movie.domain.models.MovieCast
import com.example.movie.domain.models.MovieCastPerson

class MovieCastConverter {
    fun convert(response: MovieCastResponse): MovieCast {
        return with(response) {
            MovieCast(
                imdbId = this.imDbId,
                fullTitle = this.title,
                directors = convertDirectors(this.directors),
                writers = convertWriters(this.writers),
                actors = convertActors(this.actors),
                others = convertOthers(this.others)
            )
        }
    }

    private fun convertOthers(othersResponses: List<OtherResponse>): List<MovieCastPerson> {
        return othersResponses.flatMap { otherResponse ->
            otherResponse.items.map { it.toMovieCastPerson(jobPrefix = otherResponse.job) }
        }
    }

    private fun convertActors(actors: List<ActorResponse>): List<MovieCastPerson> {
        return actors.map {
            MovieCastPerson(
                id = it.id,
                name = it.name,
                description = it.asCharacter,
                image = it.image
            )
        }
    }

    private fun convertWriters(writers: WritersResponse): List<MovieCastPerson> {
        return writers.items.map { it.toMovieCastPerson() }
    }

    private fun convertDirectors(directors: DirectorsResponse): List<MovieCastPerson> {
        return directors.items.map { it.toMovieCastPerson() }
    }

    private fun CastItemResponse.toMovieCastPerson(jobPrefix: String = ""): MovieCastPerson {
        return MovieCastPerson(
            id = this.id,
            name = this.name,
            description = if (jobPrefix.isEmpty()) this.description else "$jobPrefix -- ${this.description}",
            image = null
        )
    }
}