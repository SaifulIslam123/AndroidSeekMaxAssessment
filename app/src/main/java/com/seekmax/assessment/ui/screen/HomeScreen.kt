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
import com.seekmax.assessment.ActiveQuery
import com.seekmax.assessment.model.ActiveJob
import com.seekmax.assessment.model.toActiveJob
import com.seekmax.assessment.repository.NetworkResult
import com.seekmax.assessment.ui.theme.backgroundSecondary
import com.seekmax.assessment.ui.theme.textPrimary
import com.seekmax.assessment.ui.theme.textSecondary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun HomeScreen(navController: NavController) {

    val vm: HomeScreenViewModel = hiltViewModel()
    vm.getActiveJobResponse()
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

                vm.getActiveJobResponse()
                val activeJobList by vm.activeJobStateFlow.collectAsStateWithLifecycle()

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(activeJobList.data ?: emptyList()) {
                        JobItemView(it)
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

    val activeJobStateFlow =
        MutableStateFlow<NetworkResult<List<ActiveJob>>>(NetworkResult.Loading())

    fun getActiveJobResponse() = viewModelScope.launch {
       val response = homeRepository.getActiveJobList()
        activeJobStateFlow.value = response
    }

}

class HomeRepository @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getActiveJobList(): NetworkResult<List<ActiveJob>> {

        val response = apolloClient.query(ActiveQuery()).execute()
        val res = response.data?.active?.jobs
        val result = res?.map {
            it.toActiveJob()
        } ?: emptyList()


        return NetworkResult.Success(data = result)
    }

}