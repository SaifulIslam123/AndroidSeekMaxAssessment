package com.seekmax.assessment.ui.screen.myjobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seekmax.assessment.fragment.JobInfo
import com.seekmax.assessment.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyJobsViewModel @Inject constructor(private val repository: MyJobsRepository) :
    ViewModel() {

    private val _myJobListStateFlow =
        MutableStateFlow<NetworkResult<List<JobInfo>>>(NetworkResult.Empty())
    val myJobListStateFlow = _myJobListStateFlow.asStateFlow()

    fun getMyJobList() = viewModelScope.launch {
        repository.getMyApplyJobList().collect {
            _myJobListStateFlow.value = it
        }
    }

}

