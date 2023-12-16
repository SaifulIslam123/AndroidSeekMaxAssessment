package com.seekmax.assessment.ui.screen.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.seekmax.assessment.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(preferences: SharedPreferences) :
    ViewModel() {
    val loginState = MutableStateFlow(preferences.getString(USER_TOKEN, "")?.isNotEmpty())
}