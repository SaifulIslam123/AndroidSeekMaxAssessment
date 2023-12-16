package com.seekmax.assessment.ui.screen

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.seekmax.assessment.USER_TOKEN
import com.seekmax.assessment.ui.theme.button
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun ProfileScreen(navController: NavController) {

    val viewModel: ProfileViewModel = hiltViewModel()
    val login by viewModel.loginState.collectAsStateWithLifecycle()
    if (login == true) ProfileView() else NonLoginView(navController)
}

@Composable
fun NonLoginView(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        Text(text = "Be seen with a Jobstreet profile", style = MaterialTheme.typography.button)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Receive job opportunities, manage your resumes and apply for roles faster",
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                navController.navigate(BottomNavigationScreens.Login.route)
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = button)
        ) {
            Text("Login", color = Color.White)
        }
    }
}

@Composable
fun ProfileView() {
    Text(text = "you Login", style = MaterialTheme.typography.h6)
}


@HiltViewModel
class ProfileViewModel @Inject constructor(private var preferences: SharedPreferences) :
    ViewModel() {

    init {
        Log.d("profileTOken:", "${preferences.getString(USER_TOKEN, "")}")
    }

    val loginState = MutableStateFlow(preferences.getString(USER_TOKEN, "")?.isNotEmpty())
    fun login() {
        viewModelScope.launch {
            delay(3000)
            preferences.edit().putString(USER_TOKEN, "token").apply()
            loginState.value = preferences.getString(USER_TOKEN, "")?.isNotEmpty()
        }
    }
}