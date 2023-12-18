package com.seekmax.assessment.ui.screen.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seekmax.assessment.USER_NAME
import com.seekmax.assessment.USER_TOKEN
import com.seekmax.assessment.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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