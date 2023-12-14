package com.seekmax.assessment.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.seekmax.assessment.repository.User
import com.seekmax.assessment.repository.UserRepository
import com.seekmax.assessment.ui.theme.backgroundSecondary
import com.seekmax.assessment.ui.theme.textPrimary
import com.seekmax.assessment.ui.theme.textSecondary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val vm: HomeScreenViewModel = hiltViewModel()

    Scaffold(
        /*topBar = {
        TopAppBar(title = {
            Text(text = "Users")
        })
    }, */content = { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(backgroundSecondary)
        ) {
            /*val users by vm.users.collectAsStateWithLifecycle()
            users.forEach { user ->
                ClickableText(text = AnnotatedString(user.name), Modifier.padding(all = 16.dp),
                    onClick = {
                        navController.navigate("users/${user.id}")
                    })
            }*/

            val list = listOf("Job-1", "Job-2", "Job-3", "Job-4", "Job-5")

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(list) {
                    JobItemView(it)
                }
            }


        }


    })
}

@Composable
fun JobItemView(data: String) {
    Card(
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { Log.d("test", "card click $data") }, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "$data",
                    color = textPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    Icons.Default.Lock,
                    contentDescription = ""
                )
            }
            Text(
                "Company name 1", color = textSecondary, style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                "At present all cards look smashed together. Let’s give some padding between each card and padding on top of the first card, bottom of last card, left of all",
                color = textPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _users = MutableStateFlow(userRepository.getUsers())
    val users: StateFlow<List<User>> = _users
}