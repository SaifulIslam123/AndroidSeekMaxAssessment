package com.seekmax.assessment.ui.screen.myjobs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.seekmax.assessment.repository.NetworkResult
import com.seekmax.assessment.ui.component.JobList
import com.seekmax.assessment.ui.component.NonLoginView
import com.seekmax.assessment.ui.screen.home.JobListViewModel
import com.seekmax.assessment.ui.screen.profile.ProfileViewModel

@Composable
fun MyJobListScreen(navController: NavController) {

    val viewModel: ProfileViewModel = hiltViewModel()
    val loginStateFlow by viewModel.loginSateFlow.collectAsStateWithLifecycle()
    if (loginStateFlow) ShowMyJobList(navController) else NonLoginView(navController)
}

@Composable
fun ShowMyJobList(navController: NavController) {
    val viewModel: JobListViewModel = hiltViewModel()

    LaunchedEffect(true) {
        viewModel.getActiveJobList()
    }
    val activeJobList by viewModel.activeJobListStateFlow.collectAsStateWithLifecycle()
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

