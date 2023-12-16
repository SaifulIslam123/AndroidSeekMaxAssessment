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

    private val TAG = "HomeViewModel"

    val activeJobListState =
        MutableStateFlow<NetworkResult<List<ActiveQuery.Job>>>(NetworkResult.Empty())

    init {
        Log.d(TAG, "init")
        getActiveJobList(null)
    }

    fun getActiveJobList(search: String? = null) = viewModelScope.launch {
        homeRepository.getActiveJobList(search ?: "").collect {
            activeJobListState.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }
}