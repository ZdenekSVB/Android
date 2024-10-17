package com.example.mystery.communication

import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface IBaseRemoteRepository {
    suspend fun <T : Any> handleApiCall(call: Response<T>): CommunicationResult<T>{
        return try {
            if (call.isSuccessful) {
                call.body()?.let {
                    CommunicationResult.Success(it)
                } ?: CommunicationResult.Error(CommunicationError(call.code(), call.message()))
            } else {
                CommunicationResult.Error(CommunicationError(call.code(), call.message()))
            }
        } catch (ex: UnknownHostException) {
            CommunicationResult.Exception(ex)
        } catch (ex: SocketTimeoutException) {
            CommunicationResult.ConnectionError()
        } catch (ex: Exception) {
            CommunicationResult.Exception(ex)
        }
    }

}