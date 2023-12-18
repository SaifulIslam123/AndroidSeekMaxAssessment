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
import com.seekmax.assessment.ui.screen.profile.ProfileViewModel

@Composable
fun MyJobsScreen(navController: NavController) {

    val viewModel: ProfileViewModel = hiltViewModel()
    val loginStateFlow by viewModel.loginSateFlow.collectAsStateWithLifecycle()
    if (loginStateFlow) ShowMyJobList(navController) else NonLoginView(navController)
}

@Composable
fun ShowMyJobList(navController: NavController) {
    val viewModel: MyJobsViewModel = hiltViewModel()

    LaunchedEffect(true) {
        viewModel.getMyJobList()
    }
    val myJobList by viewModel.myJobListStateFlow.collectAsStateWithLifecycle()
    when (myJobList) {
        is NetworkResult.Success -> {
            myJobList.data?.let {
                JobList(navController = navController, jobList = it, false)
            }
        }

        else -> {
        }
    }

}

