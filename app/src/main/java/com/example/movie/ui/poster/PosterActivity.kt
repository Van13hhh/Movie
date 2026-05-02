package com.example.movie.ui.poster

import android.app.Activity
import android.os.Bundle
import com.example.movie.R
import com.example.movie.util.Creator

class PosterActivity : Activity() {

    private val posterConroller = Creator.providePosterConroller(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster)
        posterConroller.onCreate()
    }
}