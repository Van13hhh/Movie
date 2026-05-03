package com.example.movie.ui.poster

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.presentation.poster.PosterPresenter
import com.example.movie.presentation.poster.PosterView
import com.example.movie.util.Creator

class PosterActivity : Activity(), PosterView {
    private lateinit var url: String
    lateinit var posterPresenter: PosterPresenter
    lateinit var poster: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster)
        poster = findViewById(R.id.poster)
        url = intent.extras?.getString("poster", "") ?: ""
        posterPresenter = Creator.providePosterPresenter(this, url)
        posterPresenter.onCreate()
    }

    override fun showPoster(url: String) {
        Glide.with(applicationContext)
            .load(url)
            .into(poster)
    }
}