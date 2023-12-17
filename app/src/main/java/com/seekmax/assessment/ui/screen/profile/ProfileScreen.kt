package com.seekmax.assessment.ui.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.seekmax.assessment.ui.screen.BottomNavigationScreens
import com.seekmax.assessment.ui.screen.login.Credentials
import com.seekmax.assessment.ui.theme.button

@Composable
fun ProfileScreen(navController: NavController) {

    val viewModel: ProfileViewModel = hiltViewModel()
    if (viewModel.isUserLoggedIn()) ProfileView(viewModel) else NonLoginView(navController)
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
fun ProfileView(viewModel: ProfileViewModel) {
    var nameState by remember { mutableStateOf("") }
    var newPasswordState by remember { mutableStateOf("") }
    var confirmPasswordState by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Text(text = viewModel.getUserName(), style = MaterialTheme.typography.h6)
            }

        }
        Text(
            text = "Change Name",
            modifier = Modifier.padding(top = 20.dp),
            style = MaterialTheme.typography.h6
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            value = nameState,
            onValueChange = { nameState = it },
            placeholder = { Text(text = "Change your name") })
        Button(
            onClick = {
                // viewModel.login(credentialsState.userName, credentialsState.password)
            },
            enabled = nameState.isNotEmpty(),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = button)
        ) {
            Text("Change Name", color = Color.White)
        }
        Text(
            text = "Change password",
            modifier = Modifier.padding(top = 30.dp),
            style = MaterialTheme.typography.h6
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            value = newPasswordState,
            onValueChange = { confirmPasswordState = it },
            placeholder = { Text(text = "New password") })
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            value = confirmPasswordState,
            onValueChange = { confirmPasswordState = it },
            placeholder = { Text(text = "Confirm password") })
        Button(
            onClick = {
                // viewModel.login(credentialsState.userName, credentialsState.password)
            },
            enabled =,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = button)
        ) {
            Text("Change password", color = Color.White)
        }

    }


}