package com.seekmax.assessment.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seekmax.assessment.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    private val _loginStateFlow = MutableStateFlow<NetworkResult<String>>(NetworkResult.Empty())
    val loginStateFlow = MutableStateFlow<NetworkResult<String>>(NetworkResult.Empty())

    fun login(userName: String, password: String) = viewModelScope.launch {
        repository.login(userName, password).collect {
            _loginStateFlow.value = it
        }
    }
}
