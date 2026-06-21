package com.example.movie.ui.cast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movie.databinding.FragmentMoviesCastBinding
import com.example.movie.ui.cast.view_model.MovieCastViewModel
import com.example.movie.ui.cast.view_model.MoviesCastState
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class MoviesCastFragment : Fragment() {

    companion object {
        const val KEY = "movie_id"
        const val TAG = "CAST_TAG"

        fun newInstance(movieId: String): MoviesCastFragment {
            return MoviesCastFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY, movieId)
                }
            }
        }
    }

    private lateinit var binding: FragmentMoviesCastBinding

    private val moviesCastViewModel: MovieCastViewModel by viewModel {
        parametersOf(requireArguments().getString(KEY))
    }

    private val adapter = ListDelegationAdapter(
        movieCastHeaderDelegate(),
        movieCastPersonDelegate(),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesCastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.moviesCastRecyclerView.adapter = adapter
        binding.moviesCastRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        moviesCastViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
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