package com.example.movie.ui.actor.view_model

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.domain.models.ActorCast

class ActorAdapter(private val clickListener: MovieClickListener) :
    RecyclerView.Adapter<ActorViewHolder>() {

    var actors = ArrayList<ActorCast>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder =
        ActorViewHolder.from(parent)

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actors[position])
        holder.itemView.setOnClickListener {
            clickListener.onActorClick(actors[position])
        }
    }

    override fun getItemCount(): Int = actors.size

    fun interface MovieClickListener {
        fun onActorClick(actor: ActorCast)
    }
}