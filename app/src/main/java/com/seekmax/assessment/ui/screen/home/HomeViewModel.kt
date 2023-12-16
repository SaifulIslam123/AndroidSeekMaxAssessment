package com.seekmax.assessment.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.seekmax.assessment.ActiveQuery
import com.seekmax.assessment.model.ActiveJob
import com.seekmax.assessment.model.toActiveJob
import com.seekmax.assessment.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    ViewModel() {

    val activeJobListState =
        MutableStateFlow<NetworkResult<List<ActiveJob>>>(NetworkResult.Empty())

    init {
        Log.d("Home", "init")
        getActiveJobList(null)
    }

    private fun getActiveJobList(search: String?) = viewModelScope.launch {
        homeRepository.getActiveJobList(search ?: "").collect {
            activeJobListState.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("Home", "onCleared")
    }

}

class HomeRepository @Inject constructor(private val apolloClient: ApolloClient) {

    fun getActiveJobList(search: String): Flow<NetworkResult<List<ActiveJob>>> {

        return flow {
            emit(NetworkResult.Loading())
            val response = apolloClient.query(
                ActiveQuery(
                    Optional.present(100),
                    Optional.present(1)
                )
            ).execute()
            val list = response.data?.active?.jobs?.map { it.toActiveJob() } ?: emptyList()
            Log.d("testjob", "list ${list.size}")
            emit(NetworkResult.Success(data = list))
        }.catch {
            emit(NetworkResult.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

}