package com.example.movie.ui.movies.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movie.databinding.ActivityMoviesBinding
import com.example.movie.domain.models.Movie
import com.example.movie.ui.movies.MoviesState
import com.example.movie.ui.movies.view_model.MoviesViewModel
import com.example.movie.ui.movies.activity.MoviesAdapter
import com.example.movie.ui.poster.activity.PosterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoviesBinding
    private val viewModel by viewModel<MoviesViewModel>()

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val adapter = MoviesAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var textWatcher: TextWatcher? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.movies.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.movies.adapter = adapter

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeShowToast().observe(this) {
            showToast(it)
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }
        }
        textWatcher?.let {binding.queryInput.addTextChangedListener(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.queryInput.removeTextChangedListener(it) }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun showLoading() {
        binding.apply {
            movies.isVisible = false
            placeholderMessage.isVisible = false
            progressBar.isVisible = true
        }
    }

    fun showError(errorMessage: String) {
        binding.apply {
            movies.visibility = View.GONE
            placeholderMessage.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            placeholderMessage.text = errorMessage
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showContent(movies: List<Movie>) {
        binding.apply {
            this.movies.isVisible = true
            placeholderMessage.isVisible = false
            progressBar.isVisible = false
        }

        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    fun render(state: MoviesState) {
        when (state) {
            MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Empty -> showError(state.message)
            is MoviesState.Error -> showError(state.errorMessage)
        }
    }

    fun showToast(additionalMessage: String?) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG).show()
    }
}