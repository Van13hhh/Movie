package com.example.movie.presentation.ui.poster

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.presentation.poster.PosterViewModel
import com.example.movie.util.Creator

class PosterActivity : AppCompatActivity() {
    lateinit var poster: ImageView
    private var viewModel: PosterViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster)
        poster = findViewById(R.id.poster)
        val url = intent.extras?.getString("poster", "") ?: ""

       viewModel = ViewModelProvider(this, PosterViewModel.getFactory(url))
           .get(PosterViewModel::class.java)

        viewModel?.observeUrl()?.observe(this){
            showPoster(url)
        }
    }

    fun showPoster(url: String) {
        Glide.with(applicationContext)
            .load(url)
            .into(poster)
    }
}