package com.example.movie.di

import com.example.movie.data.MoviesRepositoryImpl
import com.example.movie.domain.api.MoviesRepository
import com.example.movie.util.MovieCastConverter
import org.koin.dsl.module

val repositoryModule = module {

    factory { MovieCastConverter() }

    single<MoviesRepository> {
        MoviesRepositoryImpl(get(), get())
    }

}