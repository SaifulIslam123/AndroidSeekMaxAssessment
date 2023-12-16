package com.seekmax.assessment.ui.screen.jobdetail

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.HttpInfo
import com.seekmax.assessment.ApplyMutation
import com.seekmax.assessment.JobQuery
import com.seekmax.assessment.USER_TOKEN
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

    suspend fun getJobDetail(id: String): Flow<NetworkResult<JobQuery.Job>> {
        Log.d("jobdetail", "JobDetailRepository getJobDetail: ")
        return flow {
            emit(NetworkResult.Loading())
            val result = apolloClient.query(JobQuery(id)).execute()
            result.data?.job?.let {
                emit(NetworkResult.Success(data = it))
            }
        }.catch {
            emit(NetworkResult.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun applyJob(id: String): Flow<NetworkResult<Boolean>> {
        Log.d("applyJob", "applyJob: ")
        return flow {
            emit(NetworkResult.Loading())
            val response = apolloClient.mutation(ApplyMutation(id)).execute()
            response.data?.apply?.let {
                emit(NetworkResult.Success(data = it))
            }
            val result = response.data?.apply
            val statusCode = response.executionContext[HttpInfo]?.statusCode ?: 0

            if (statusCode == 200 && result == true) {
                emit(NetworkResult.Success(statusCode, result))
            } else {
                emit(NetworkResult.Error(result.toString(), code = statusCode))
            }

        }.catch {
            emit(NetworkResult.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

}