package com.seekmax.assessment.ui.screen.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seekmax.assessment.USER_NAME
import com.seekmax.assessment.USER_TOKEN
import com.seekmax.assessment.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val repository: ProfileRepository
) : ViewModel() {

    private val _userNameSateFlow = MutableStateFlow<NetworkResult<String>>(
        NetworkResult.Success(data = preferences.getString(USER_NAME, "") ?: "")
    )
    val userNameSateFlow = _userNameSateFlow.asStateFlow()

    private val _passwordSateFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Empty())
    val passwordSateFlow = _passwordSateFlow.asStateFlow()

    val loginSateFlow =
        MutableStateFlow(preferences.getString(USER_TOKEN, "")?.isNotEmpty() ?: false)

    fun updateUserName(name: String) = viewModelScope.launch {
        repository.updateUserName(name).collect {
            _userNameSateFlow.value = it
        }
    }

    fun updatePassword(password: String) = viewModelScope.launch {
        repository.updateUserPassword(password).collect { _passwordSateFlow.value = it }
    }

    fun logout() = preferences.edit().clear().apply()

}