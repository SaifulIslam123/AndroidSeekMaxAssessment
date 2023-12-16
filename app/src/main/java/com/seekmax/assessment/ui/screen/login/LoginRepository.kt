package com.seekmax.assessment.ui.screen.login

import android.content.SharedPreferences
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.HttpInfo
import com.seekmax.assessment.AuthMutation
import com.seekmax.assessment.USER_TOKEN
import com.seekmax.assessment.repository.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val preferences: SharedPreferences,
    private val apolloClient: ApolloClient
) {
    suspend fun login(userName: String, password: String): Flow<NetworkResult<String>> {

        return flow {
            emit(NetworkResult.Loading())
            val response = apolloClient.mutation(AuthMutation(userName, password)).execute()
            val statusCode = response.executionContext[HttpInfo]?.statusCode ?: 0
            val token = response.data?.auth
            if (statusCode == 200 && !token.isNullOrEmpty()) {
                preferences.edit().putString(USER_TOKEN, token).apply()
                emit(NetworkResult.Success(statusCode, token))
            } else {
                emit(NetworkResult.Error(token.toString(), code = statusCode))
            }
        }.catch {
            emit(NetworkResult.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    }
}