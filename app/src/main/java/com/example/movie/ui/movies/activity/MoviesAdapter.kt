package com.example.movie.ui.movies.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.domain.models.Movie
import com.example.movie.ui.movies.activity.MovieViewHolder

class MoviesAdapter(private val clickListener: MovieClickListener) : RecyclerView.Adapter<MovieViewHolder>() {

    var movies = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder.from(parent)

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener { clickListener.onMovieClick(movies[position]) }
    }

    override fun getItemCount(): Int = movies.size

    fun interface MovieClickListener {
        fun onMovieClick(movie: Movie)
    }
}