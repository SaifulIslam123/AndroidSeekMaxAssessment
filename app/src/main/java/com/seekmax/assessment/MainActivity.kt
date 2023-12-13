package com.seekmax.assessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seekmax.assessment.ui.theme.Practice_jetpack_composeTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val textState = remember { mutableStateOf("") }
            val scrollState = rememberScrollState()


            Practice_jetpack_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        Text(text = "demo",
                            modifier = Modifier
                                .background(Color.Red)
                        )
                        Greeting("Android", textState)
                        Spacer(modifier = Modifier.fillMaxSize(.7f))
                        Greeting(name = "Second", textState)
                        /* Box(
                             contentAlignment = Alignment.BottomCenter,
                             modifier = Modifier
                                 .fillMaxWidth(.5f)
                                 .weight(1f)
                                 .clip(CircleShape)
                                 .background(Color.Cyan)
                         ) {
                             Button(
                                 onClick = {
                                     Toast.makeText(
                                         this@MainActivity,
                                         "State: ${textState.value}",
                                         Toast.LENGTH_LONG
                                     ).show()
                                 }
                             ) {
                                 Text(text = "Click Me")
                             }
                         }*/

                    }

                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, textState: MutableState<String>) {
        Text(
            text = "$name",
            color = Color.Gray,
            fontSize = 26.sp,
            modifier = Modifier
                .clickable(onClick = {
                    textState.value = name
                })
                .background(color = Color.Yellow, shape = RectangleShape)
                .padding(16.dp),

            )

    }


}

