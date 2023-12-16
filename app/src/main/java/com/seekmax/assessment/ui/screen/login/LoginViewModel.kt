package com.seekmax.assessment.ui.screen.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.HttpInfo
import com.seekmax.assessment.AuthMutation
import com.seekmax.assessment.USER_TOKEN
import com.seekmax.assessment.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    val loginState = MutableStateFlow<NetworkResult<String>>(NetworkResult.Empty())

    fun login(userName: String, password: String) = viewModelScope.launch {
        repository.login(userName, password).collect {
            loginState.value = it
        }
    }
}
