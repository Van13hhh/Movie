package com.example.movie.ui.cast.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movie.databinding.MoviesCastBinding
import com.example.movie.ui.cast.movieCastHeaderDelegate
import com.example.movie.ui.cast.movieCastPersonDelegate
import com.example.movie.ui.cast.view_model.MovieCastViewModel
import com.example.movie.ui.cast.view_model.MoviesCastState
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CastActivity: AppCompatActivity() {
    private lateinit var binding: MoviesCastBinding

    private val moviesCastViewModel: MovieCastViewModel by viewModel {
        parametersOf(intent.getStringExtra("movie_id"))
    }

    private val adapter = ListDelegationAdapter(
        movieCastHeaderDelegate(),
        movieCastPersonDelegate(),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MoviesCastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moviesCastRecyclerView.adapter = adapter
        binding.moviesCastRecyclerView.layoutManager = LinearLayoutManager(this)

        moviesCastViewModel.observeState().observe(this){
            when(it){
                is MoviesCastState.Content -> showContent(it)
                is MoviesCastState.Error -> showError(it)
                MoviesCastState.Loading -> showLoading()
            }
        }
    }

    private fun showLoading() {
        binding.contentContainer.isVisible = false
        binding.errorMessageTextView.isVisible = false

        binding.progressBar.isVisible = true
    }

    private fun showError(state: MoviesCastState.Error) {
        binding.contentContainer.isVisible = false
        binding.progressBar.isVisible = false

        binding.errorMessageTextView.isVisible = true
        binding.errorMessageTextView.text = state.message
    }

    private fun showContent(state: MoviesCastState.Content) {
        binding.progressBar.isVisible = false
        binding.errorMessageTextView.isVisible = false

        binding.contentContainer.isVisible = true

        binding.movieTitle.text = state.fullTitle
        adapter.items = state.items

        adapter.notifyDataSetChanged()
    }
}