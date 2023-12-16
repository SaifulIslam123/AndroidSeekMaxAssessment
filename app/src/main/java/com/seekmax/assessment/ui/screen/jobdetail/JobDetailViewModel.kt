package com.seekmax.assessment.ui.screen.jobdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seekmax.assessment.model.Job
import com.seekmax.assessment.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobDetailViewModel @Inject constructor(private val repository: JobDetailRepository) :
    ViewModel() {

    val jobState = MutableStateFlow<NetworkResult<Job>>(NetworkResult.Empty())

    fun getJobDetail(id: String) = viewModelScope.launch {
        Log.d("jobdetail", "ViewModel getJobDetail: ")
        repository.getJobDetail(id).collect {
            jobState.value = it
        }
    }
}
