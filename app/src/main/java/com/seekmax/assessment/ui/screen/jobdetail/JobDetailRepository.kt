package com.seekmax.assessment.ui.screen.jobdetail

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.seekmax.assessment.JobQuery
import com.seekmax.assessment.model.Job
import com.seekmax.assessment.model.toJob
import com.seekmax.assessment.repository.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class JobDetailRepository @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getJobDetail(id: String): Flow<NetworkResult<Job>> {
        Log.d("jobdetail", "JobDetailRepository getJobDetail: ")
        return flow {
            emit(NetworkResult.Loading())
            val result = apolloClient.query(JobQuery(id)).execute()
            result.data?.job?.let {
                emit(NetworkResult.Success(data = it.toJob()))
            }
        }.catch {
            emit(NetworkResult.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }
}