package com.seekmax.assessment.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seekmax.assessment.fragment.JobInfo
import com.seekmax.assessment.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@FlowPreview
@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    ViewModel() {

    private val TAG = "HomeViewModel"

    val activeJobListStateFlow =
        MutableStateFlow<NetworkResult<List<JobInfo>>>(NetworkResult.Empty())

    val searchStateFlow = MutableStateFlow("")

    val searchResult = viewModelScope.launch {
        searchStateFlow.debounce(1000)
            .distinctUntilChanged()
            .flatMapLatest {
                homeRepository.getJobList(it)
            }.collect {
                activeJobListStateFlow.value = it
            }
    }


    fun getActiveJobList() = viewModelScope.launch {

        homeRepository.getJobList().collect {
            Log.d(TAG, "getActiveJobList ${it}")
            activeJobListStateFlow.value = it
        }
    }

}