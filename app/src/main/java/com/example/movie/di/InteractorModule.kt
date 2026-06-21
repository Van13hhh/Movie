package com.example.movie.di

import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.impl.MoviesInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<MoviesInteractor> {
        MoviesInteractorImpl(get())
    }

}