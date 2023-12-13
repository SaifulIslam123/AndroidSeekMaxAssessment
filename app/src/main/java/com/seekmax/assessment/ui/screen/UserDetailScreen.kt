package com.seekmax.assessment.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.seekmax.assessment.repository.User
import com.seekmax.assessment.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(navController: NavController, userId: Long) {

    val vm: UserDetailScreenViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "User detail")
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                vm.load(userId = userId)
                val user by vm.user.collectAsState()
                Column(Modifier.padding(all = 16.dp)) {
                    Text(
                        text = "Hello, I'm ${user?.name ?: ""}",
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "My email is ${user?.email ?: ""}")
                }
            }
        }
    )

}

@HiltViewModel
class UserDetailScreenViewModel @Inject constructor(val userRepository: UserRepository) :
    ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun load(userId: Long) {
        viewModelScope.launch {
            _user.value = userRepository.getUser(userId)
        }
    }

    fun refresh() {
        _user.value = null
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("UserDetailScreenViewModel", "onCleared")
    }

}