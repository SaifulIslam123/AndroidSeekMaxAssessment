package com.seekmax.assessment.ui.screen.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.seekmax.assessment.USER_NAME
import com.seekmax.assessment.USER_TOKEN
import com.seekmax.assessment.UpdatePasswordMutation
import com.seekmax.assessment.UpdateUsernameMutation
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
class ProfileViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val repository: ProfileRepository
) : ViewModel() {

    var userNameSateFlow = MutableStateFlow<NetworkResult<String>>(
        NetworkResult.Success(data = preferences.getString(USER_NAME, "") ?: "")
    )
    var passwordSateFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Empty())

    var loginSateFlow =
        MutableStateFlow(preferences.getString(USER_TOKEN, "")?.isNotEmpty() ?: false)

    fun updateUserName(name: String) = viewModelScope.launch {
        repository.updateUserName(name).collect {
            userNameSateFlow.value = it
        }
    }

    fun updatePassword(password: String) = viewModelScope.launch {
        repository.updateUserPassword(password).collect { passwordSateFlow.value = it }
    }

    fun logout() = preferences.edit().clear().apply()

}

class ProfileRepository @Inject constructor(
    private val preferences: SharedPreferences,
    private val apolloClient: ApolloClient
) {

    fun updateUserName(name: String): Flow<NetworkResult<String>> {
        return flow {
            emit(NetworkResult.Loading())
            val response = apolloClient.mutation(UpdateUsernameMutation(name)).execute()
            if (response.data?.updateUsername == true) {
                preferences.edit().putString(USER_NAME, name).apply()
                emit(NetworkResult.Success(data = name))
            } else {
                emit(NetworkResult.Error(message = "Update name failed"))
            }
        }.catch {
            emit(NetworkResult.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    fun updateUserPassword(password: String): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            val response = apolloClient.mutation(UpdatePasswordMutation(password)).execute()
            if (response.data?.updatePassword == true) {
                emit(NetworkResult.Success(data = true))
            } else {
                emit(NetworkResult.Error(message = "Update password failed"))
            }
        }.catch {
            emit(NetworkResult.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

}