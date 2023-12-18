package com.seekmax.assessment.ui.screen.profile

import android.content.SharedPreferences
import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Operation
import com.seekmax.assessment.USER_NAME
import com.seekmax.assessment.UpdatePasswordMutation
import com.seekmax.assessment.UpdateUsernameMutation
import com.seekmax.assessment.repository.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val preferences: SharedPreferences,
    private val apolloClient: ApolloClient
) {

    fun updateUserName(name: String): Flow<NetworkResult<String>> {
        return flow {
            emit(NetworkResult.Loading())
            val response = apolloClient.mutation(UpdateUsernameMutation(name)).execute()
            if (response.data?.updateUsername == true) {
                preferences.edit().putString(USER_NAME, name).apply()
                emit(NetworkResult.Success(data = name))
            } else {
                emit(NetworkResult.Error(message = "Update name failed"))
            }
        }.catch {
            emit(NetworkResult.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    fun updateUserPassword(password: String): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            val response = apolloClient.mutation(UpdatePasswordMutation(password)).execute()
            if (response.data?.updatePassword == true) {
                emit(NetworkResult.Success(data = true))
            } else {
                emit(NetworkResult.Error(message = "Update password failed"))
            }
        }.catch {
            emit(NetworkResult.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun <D> getApiResult(apiCall: suspend () -> ApolloCall<Operation.Data>): NetworkResult<Operation.Data> {
        val response = apiCall.invoke().execute()
        return response.data?.let {
            NetworkResult.Success(data = it)
        } ?: run {
            NetworkResult.Error(message = "")
        }
    }


}