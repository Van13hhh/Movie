package com.example.movie.ui.cast

import com.example.movie.domain.models.MovieCastPerson
import com.example.movie.ui.RVItem

sealed interface MoviesCastRVItem : RVItem {

    data class HeaderItem(
        val headerText: String
    ) : MoviesCastRVItem

    data class PersonItem(
        val data: MovieCastPerson
    ) : MoviesCastRVItem
}