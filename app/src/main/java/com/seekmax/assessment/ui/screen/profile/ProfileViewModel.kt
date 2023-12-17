package com.seekmax.assessment.ui.screen.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.seekmax.assessment.USER_NAME
import com.seekmax.assessment.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val preferences: SharedPreferences) :
    ViewModel() {
    fun isUserLoggedIn() = preferences.getString(USER_TOKEN, "")?.isNotEmpty() ?: false
    fun getUserName() = preferences.getString(USER_NAME, "") ?: ""

}