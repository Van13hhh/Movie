package com.example.movie.ui.movies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.Creator
import com.example.movie.R
import com.example.movie.domain.api.MoviesInteractor
import com.example.movie.domain.models.Movie
import com.example.movie.ui.poster.PosterActivity
class MoviesActivity : Activity() {
    private val moviesInteractor = Creator.provideMoviesInteractor(this)

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val movies = ArrayList<Movie>()

    private val adapter = MoviesAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val moviesSearchController = Creator.provideMoviesSearchController(this, adapter)
    private val searchRunnable = Runnable { searchRequest() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        moviesSearchController.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        moviesSearchController.onDestroy()
    }

    private fun searchRequest() {
        if (queryInput.text.isNotEmpty()) {

            placeholderMessage.visibility = View.GONE
            moviesList.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            moviesInteractor.searchMovies(queryInput.text.toString(), object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>) {
                    handler.post {
                        progressBar.visibility = View.GONE
                        movies.clear()
                        movies.addAll(foundMovies)
                        moviesList.visibility = View.VISIBLE
                        adapter.notifyDataSetChanged()
                        if (movies.isEmpty()) {
                            showMessage(getString(R.string.nothing_found), "")
                        } else {
                            hideMessage()
                        }
                    }
                }
            })
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            movies.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }

    private fun hideMessage() {
        placeholderMessage.visibility = View.GONE
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}