package com.seekmax.assessment.ui.screen

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
import androidx.navigation.NavController
import com.seekmax.assessment.ui.theme.button
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@Composable
fun ProfileScreen(navController: NavController) {

    val viewModel: ProfileViewModel = hiltViewModel()
    viewModel.login()
    val login by viewModel.loginState.collectAsStateWithLifecycle()
    if (login) ProfileView() else NonLoginView()
}

@Composable
fun NonLoginView() {
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
class ProfileViewModel @Inject constructor() :
    ViewModel() {

    val loginState = MutableStateFlow(false)
    fun login() {
        loginState.value = false
    }
}