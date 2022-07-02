package com.sdk.mviarch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdk.mviarch.network.ApiService
import com.sdk.mviarch.network.RetroInstance
import com.sdk.mviarch.repository.MainIntent
import com.sdk.mviarch.repository.MainState
import com.sdk.mviarch.repository.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {

    private val userRepository: UserRepository = UserRepository(RetroInstance.apiService)

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.Init)
    val state: StateFlow<MainState> get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchUser -> fetchUsers()
                }
            }
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value = try {
                MainState.Users(userRepository.getUsers())
            } catch (e: Exception) {
                MainState.Error(e.stackTraceToString())
            }
        }
    }
}