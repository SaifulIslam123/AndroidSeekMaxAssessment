package com.seekmax.assessment.ui.screen.myjobs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.seekmax.assessment.repository.NetworkResult
import com.seekmax.assessment.ui.ProgressHelper
import com.seekmax.assessment.ui.component.JobList
import com.seekmax.assessment.ui.component.NonLoginView
import com.seekmax.assessment.ui.screen.profile.ProfileViewModel

@Composable
fun MyJobsScreen(navController: NavController) {
    val viewModel: MyJobsViewModel = hiltViewModel()
    if (viewModel.isUserLoggedIn()) {
        ShowMyJobList(navController, viewModel)
    } else NonLoginView(
        navController
    )
}

@Composable
fun ShowMyJobList(navController: NavController, viewModel: MyJobsViewModel) {

    LaunchedEffect(true) {
        viewModel.getMyJobList()
    }
    val myJobList by viewModel.myJobListStateFlow.collectAsStateWithLifecycle()
    when (myJobList) {
        is NetworkResult.Success -> {
            ProgressHelper.dismissDialog()
            myJobList.data?.let {
                JobList(navController = navController, jobList = it, false)
            }
        }

        is NetworkResult.Loading -> ProgressHelper.showDialog(LocalContext.current)
        is NetworkResult.Error -> ProgressHelper.dismissDialog()

        else -> {
        }
    }

}

