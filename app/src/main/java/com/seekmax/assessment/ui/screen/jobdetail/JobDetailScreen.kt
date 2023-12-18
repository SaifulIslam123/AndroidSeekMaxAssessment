package com.seekmax.assessment.ui.screen.jobdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.seekmax.assessment.JobQuery
import com.seekmax.assessment.R
import com.seekmax.assessment.RELOAD_DATA
import com.seekmax.assessment.repository.NetworkResult
import com.seekmax.assessment.ui.ProgressHelper
import com.seekmax.assessment.ui.screen.BottomNavigationScreens
import com.seekmax.assessment.ui.theme.button
import com.seekmax.assessment.ui.theme.textPrimary


@Composable
fun JobDetailScreen(navController: NavController, jobId: String) {

    val viewModel: JobDetailViewModel = hiltViewModel()
    val jobDetailStateFlow by viewModel.jobDetailStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.getJobDetail(jobId)
    }

    Scaffold(content = {
        Surface(modifier = Modifier.padding(it)) {

            when (jobDetailStateFlow) {
                is NetworkResult.Success -> {
                    ProgressHelper.dismissDialog()
                    ShowJobDetail(navController, jobDetailStateFlow.data!!, viewModel)
                }

                is NetworkResult.Loading -> ProgressHelper.showDialog(LocalContext.current)
                is NetworkResult.Error -> ProgressHelper.dismissDialog()
                else -> {}
            }
        }
    })

}

@Composable
fun ShowJobDetail(navController: NavController, job: JobQuery.Job, viewModel: JobDetailViewModel) {

    var jobAppliedState by remember { mutableStateOf(job.haveIApplied) }
    val context = LocalContext.current

    LaunchedEffect(viewModel.applyJobSharedFlow) {
        viewModel.applyJobSharedFlow.collect {
            when (it) {
                is NetworkResult.Success -> {
                    ProgressHelper.dismissDialog()
                    jobAppliedState = true
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(RELOAD_DATA, true)
                }

                is NetworkResult.Loading -> ProgressHelper.showDialog(context)
                is NetworkResult.Error -> ProgressHelper.dismissDialog()
                else -> {}
            }
        }
    }

    Scaffold(
        content = {
            Surface(modifier = Modifier.padding(it)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(50.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            job._id,
                            color = textPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (jobAppliedState) {
                            Image(
                                painterResource(R.drawable.ic_check),
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Text(
                        "Company Name: ${job.industry}",
                        color = textPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Location: ${job.location}",
                        color = textPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Salary: ${job.salaryRange.min} - ${job.salaryRange.max}",
                        color = textPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        job.description,
                        color = textPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (!jobAppliedState) {
                        Spacer(modifier = Modifier.height(50.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = {
                                    if (viewModel.isUserLoggedIn()) {
                                        viewModel.applyJob(job._id)
                                    } else {
                                        navController.navigate(BottomNavigationScreens.Profile.route)
                                    }
                                },
                                shape = RoundedCornerShape(5.dp),
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(100.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = button)
                            ) {
                                Text("Apply", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    )
}
