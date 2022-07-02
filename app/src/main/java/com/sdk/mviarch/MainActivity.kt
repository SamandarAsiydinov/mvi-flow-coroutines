package com.sdk.mviarch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdk.mviarch.adapter.UserAdapter
import com.sdk.mviarch.databinding.ActivityMainBinding
import com.sdk.mviarch.repository.MainIntent
import com.sdk.mviarch.repository.MainState
import com.sdk.mviarch.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {
        userAdapter = UserAdapter()
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        setupRv()

        binding.btnFetch.setOnClickListener {
            lifecycleScope.launch {
                viewModel.userIntent.send(MainIntent.FetchUser)
            }
        }
        observe()
    }

    private fun setupRv() = binding.recyclerView.apply {
        val divider = DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
        addItemDecoration(divider)
        adapter = userAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is MainState.Init -> Unit
                    is MainState.Loading -> {
                        binding.btnFetch.isVisible = false
                        binding.progressBar.isVisible = true
                    }
                    is MainState.Error -> {
                        binding.btnFetch.isVisible = false
                        binding.progressBar.isVisible = false
                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_SHORT).show()
                    }
                    is MainState.Users -> {
                        binding.btnFetch.isVisible = false
                        binding.progressBar.isVisible = false
                        binding.recyclerView.isVisible = true
                        userAdapter.submitList(it.users)
                    }
                }
            }
        }
    }
}