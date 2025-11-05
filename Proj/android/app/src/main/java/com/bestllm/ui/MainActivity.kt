package com.bestllm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.bestllm.R
import com.bestllm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTopAppBar()
        setupBottomNavigation()
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, PostListFragment())
            }
        }
    }

    private fun setupTopAppBar() {
        // Title navigates to main page
        binding.topAppBar.findViewById<android.widget.TextView>(R.id.tvAppTitle).setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, PostListFragment())
            }
        }
        // Profile button navigates to ProfileFragment
        binding.topAppBar.findViewById<android.widget.ImageButton>(R.id.btnProfile).setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, ProfileFragment())
                addToBackStack(null)
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_posts -> {
                    supportFragmentManager.commit {
                        replace(R.id.fragmentContainer, PostListFragment())
                    }
                    true
                }
                R.id.nav_profile -> {
                    supportFragmentManager.commit {
                        replace(R.id.fragmentContainer, ProfileFragment())
                    }
                    true
                }
                else -> false
            }
        }
    }
}

