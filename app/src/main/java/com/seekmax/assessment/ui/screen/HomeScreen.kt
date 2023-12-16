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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.seekmax.assessment.ActiveQuery
import com.seekmax.assessment.model.ActiveJob
import com.seekmax.assessment.model.toActiveJob
import com.seekmax.assessment.repository.NetworkResult
import com.seekmax.assessment.ui.theme.backgroundSecondary
import com.seekmax.assessment.ui.theme.textPrimary
import com.seekmax.assessment.ui.theme.textSecondary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun HomeScreen(navController: NavController) {

    val viewModel: HomeScreenViewModel = hiltViewModel()
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

                val activeJobList by viewModel.activeJobListState.collectAsStateWithLifecycle()
                when (activeJobList) {
                    is NetworkResult.Loading -> {
                    }

                    is NetworkResult.Error -> {
                    }

                    is NetworkResult.Success -> {
                        activeJobList.data?.let {
                            LazyColumn(
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(it) {
                                    JobItemView(it)
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
fun JobItemView(it: ActiveJob) {


    Card(
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { Log.d("test", "card click ") }, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
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
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = ""
                    )
                }
            }
            Text(
                it.industry.toString(),
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

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    ViewModel() {

    val activeJobListState =
        MutableStateFlow<NetworkResult<List<ActiveJob>>>(NetworkResult.Empty())

    init {
        getActiveJobList(null)
    }

    private fun getActiveJobList(search: String?) = viewModelScope.launch {
        homeRepository.getActiveJobList(search ?: "").collect {
            activeJobListState.value = it
        }
    }

}

class HomeRepository @Inject constructor(private val apolloClient: ApolloClient) {

    fun getActiveJobList(search: String): Flow<NetworkResult<List<ActiveJob>>> {

        return flow {
            emit(NetworkResult.Loading())
            val response = apolloClient.query(ActiveQuery(Optional.present(100),Optional.present(1))).execute()
            val list = response.data?.active?.jobs?.map { it.toActiveJob() } ?: emptyList()
            Log.d("testjob", "list ${list.size}")
            emit(NetworkResult.Success(data = list))
        }.catch {
            emit(NetworkResult.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

}