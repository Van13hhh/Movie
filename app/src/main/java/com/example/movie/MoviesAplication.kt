package com.example.movie

import android.app.Application
import com.example.movie.di.dataModule
import com.example.movie.di.interactorModule
import com.example.movie.di.repositoryModule
import com.example.movie.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MoviesApplication)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }
}