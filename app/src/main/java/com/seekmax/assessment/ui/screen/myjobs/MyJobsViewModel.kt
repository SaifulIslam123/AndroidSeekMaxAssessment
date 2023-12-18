package com.seekmax.assessment.ui.screen.myjobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seekmax.assessment.fragment.JobInfo
import com.seekmax.assessment.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyJobsViewModel @Inject constructor(private val repository: MyJobsRepository) :
    ViewModel() {

    var myJobListStateFlow = MutableStateFlow<NetworkResult<List<JobInfo>>>(NetworkResult.Empty())

    fun getMyJobList() = viewModelScope.launch {
        repository.getMyApplyJobList().collect {
            myJobListStateFlow.value = it
        }
    }

}

