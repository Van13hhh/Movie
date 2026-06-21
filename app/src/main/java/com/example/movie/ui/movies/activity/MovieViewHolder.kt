package com.example.movie.ui.movies.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.databinding.ListItemMovieBinding
import com.example.movie.domain.models.Movie

class MovieViewHolder(private val binding: ListItemMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: Movie) {
        Glide.with(itemView)
            .load(movie.image)
            .into(binding.cover)

        binding.title.text = movie.title
        binding.description.text = movie.description
    }

    companion object {
        fun from(parent: ViewGroup): MovieViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemMovieBinding.inflate(inflater, parent, false)
            return MovieViewHolder(binding)
        }
    }
}