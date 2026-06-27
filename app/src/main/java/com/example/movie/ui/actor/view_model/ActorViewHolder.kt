package com.example.movie.ui.actor.view_model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.databinding.ListItemActorBinding
import com.example.movie.domain.models.ActorCast

class ActorViewHolder(private val binding: ListItemActorBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(actor: ActorCast) {
        Glide.with(itemView)
            .load(actor.imageUrl)
            .circleCrop()
            .into(binding.cover)

        binding.title.text = actor.nameActor
        binding.description.text = actor.description
    }

    companion object {
        fun from(parent: ViewGroup): ActorViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemActorBinding.inflate(inflater, parent, false)
            return ActorViewHolder(binding)
        }
    }
}