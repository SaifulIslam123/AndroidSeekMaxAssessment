package com.seekmax.assessment.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.seekmax.assessment.RELOAD_DATA
import com.seekmax.assessment.repository.NetworkResult
import com.seekmax.assessment.ui.component.JobList
import com.seekmax.assessment.ui.theme.backgroundSecondary

@Composable
fun HomeScreen(navController: NavController) {

    val viewModel: HomeViewModel = hiltViewModel()
    val activeJobList by viewModel.activeJobListStateFlow.collectAsStateWithLifecycle()
    val searchStateFlow by viewModel.searchStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        /*if (activeJobList is NetworkResult.Empty)
            viewModel.getActiveJobList()
*/
        if (navController.currentBackStackEntry!!.savedStateHandle.contains(RELOAD_DATA)) {
            val reloadData =
                navController.currentBackStackEntry!!.savedStateHandle.get<Boolean>(
                    RELOAD_DATA
                ) ?: false
            if (reloadData) {
                viewModel.getActiveJobList()
            }
        }
    }

    Scaffold(
        /*topBar = {
        TopAppBar(title = {
            Text(text = "Users")
        })
    }, */content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundSecondary)
                    .padding(padding)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = searchStateFlow,
                    onValueChange = { viewModel.searchStateFlow.value = it },
                    placeholder = { Text(text = "Search Job") })

                when (activeJobList) {
                    is NetworkResult.Success -> {
                        activeJobList.data?.let {
                            JobList(navController = navController, jobList = it)
                        }
                    }

                    else -> {
                    }
                }
            }
        })
}
