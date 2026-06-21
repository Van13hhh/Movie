package com.example.movie.presentation.movies

import com.example.movie.ui.movies.MoviesState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MoviesView: MvpView{
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: MoviesState)

    // One-time event methods
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(additionalMessage: String)
}