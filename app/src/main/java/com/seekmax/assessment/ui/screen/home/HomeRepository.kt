package com.seekmax.assessment.ui.screen.home

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.seekmax.assessment.ActiveQuery
import com.seekmax.assessment.repository.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apolloClient: ApolloClient) {

    fun getActiveJobList(search: String): Flow<NetworkResult<List<ActiveQuery.Job>>> {

        return flow {
            emit(NetworkResult.Loading())
            val response = apolloClient.query(
                ActiveQuery(
                    Optional.present(100),
                    Optional.present(1)
                )
            ).execute()
            //val list = response.data?.active?.jobs?.map { it.toActiveJob() } ?: emptyList()
            val list = response.data?.active?.jobs
            emit(NetworkResult.Success(data = list))
        }.catch {
            emit(NetworkResult.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

}