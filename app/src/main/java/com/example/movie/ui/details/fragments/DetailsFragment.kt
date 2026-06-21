package com.example.movie.ui.details.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movie.R
import com.example.movie.databinding.FragmentDetailsBinding
import com.example.movie.ui.details.DetailsViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailsFragment: Fragment() {

    companion object{
        private const val ARGS_MOVIE_ID = "movie_id"
        private const val ARGS_POSTER_URL = "poster_url"

        const val TAG = "DetailsFragment"

        fun newInstance(movieId: String, posterUrl: String): Fragment {
            return DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGS_MOVIE_ID, movieId)
                    putString(ARGS_POSTER_URL, posterUrl)
                }
            }
        }
    }
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val poster = requireArguments().getString(ARGS_POSTER_URL) ?: ""
        val movieId = requireArguments().getString(ARGS_MOVIE_ID) ?: ""

        binding.viewPager.adapter = DetailsViewPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
            posterUrl = poster,
            movieId = movieId,
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.poster)
                1 -> tab.text = getString(R.string.details)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}