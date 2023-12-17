package com.seekmax.assessment.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.seekmax.assessment.R
import com.seekmax.assessment.RELOAD_DATA
import com.seekmax.assessment.fragment.JobInfo
import com.seekmax.assessment.repository.NetworkResult
import com.seekmax.assessment.ui.screen.BottomNavigationScreens
import com.seekmax.assessment.ui.theme.backgroundSecondary
import com.seekmax.assessment.ui.theme.textPrimary
import com.seekmax.assessment.ui.theme.textSecondary

@Composable
fun HomeScreen(navController: NavController) {

    val viewModel: HomeScreenViewModel = hiltViewModel()
    val activeJobList by viewModel.activeJobListState.collectAsStateWithLifecycle()
    val searchStateFlow by viewModel.searchStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        if (activeJobList is NetworkResult.Empty)
            viewModel.getActiveJobList()

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
                            LazyColumn(
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(it) {
                                    JobItemView(navController, it)
                                }
                            }
                        }
                    }

                    else -> {
                    }
                }
            }
        })
}

@Composable
fun JobItemView(navController: NavController, it: JobInfo) {

    Card(
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("${BottomNavigationScreens.JobDetail.routePrefix}${it._id}")
            }, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    it.positionTitle,
                    color = textPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (it.haveIApplied) {
                    Image(
                        painterResource(R.drawable.ic_check),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)

                    )
                }
            }
            Text(
                it.industry,
                color = textSecondary,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                it.description,
                color = textPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

}