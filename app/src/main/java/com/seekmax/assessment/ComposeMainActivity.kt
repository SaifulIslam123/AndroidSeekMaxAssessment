package com.seekmax.assessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seekmax.assessment.ui.screen.HomeScreen
import com.seekmax.assessment.ui.screen.HomeScreenViewModel
import com.seekmax.assessment.ui.screen.LoginScreen
import com.seekmax.assessment.ui.screen.MainScreen
import com.seekmax.assessment.ui.screen.UserDetailScreen
import com.seekmax.assessment.ui.screen.UserDetailScreenViewModel
import com.seekmax.assessment.ui.theme.Practice_jetpack_composeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practice_jetpack_composeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Practice_jetpack_composeTheme {
        Greeting("Android")
    }
}