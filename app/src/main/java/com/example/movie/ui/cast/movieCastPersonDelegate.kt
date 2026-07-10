package com.example.movie.ui.cast

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.movie.databinding.ListItemCastBinding
import com.example.movie.ui.RVItem
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun movieCastPersonDelegate() =
    adapterDelegateViewBinding<MoviesCastRVItem.PersonItem, RVItem, ListItemCastBinding>(
        { layoutInflater, root -> ListItemCastBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            if (item.data.image == null) {
                binding.actorImageView.isVisible = false
            } else {
                Glide.with(itemView)
                    .load(item.data.image)
                    .into(binding.actorImageView)
                binding.actorImageView.isVisible = true
            }

            binding.actorNameTextView.text = item.data.name
            binding.actorDescriptionTextView.text = item.data.description
        }
    }