package com.seekmax.assessment.ui.screen

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.seekmax.assessment.ComposeMainActivity
import com.seekmax.assessment.repository.User
import com.seekmax.assessment.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val vm: HomeScreenViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Users")
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                val users by vm.users.collectAsStateWithLifecycle()
                users.forEach { user ->
                    ClickableText(text = AnnotatedString(user.name), Modifier.padding(all = 16.dp),
                        onClick = {
                            //    (navController.context as ComposeMainActivity).udVM.refresh()
                            navController.navigate("users/${user.id}")
                        })
                }
            }
        }
    )
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _users = MutableStateFlow(userRepository.getUsers())
    val users: StateFlow<List<User>> = _users
}