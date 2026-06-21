package com.example.movie.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.movie.R
import com.example.movie.databinding.ActivityRootBinding
import com.example.movie.ui.movies.MoviesFragment

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null){
            supportFragmentManager.commit {
                this.add(R.id.rootFragmentContainerView, MoviesFragment())
            }
        }
    }

}