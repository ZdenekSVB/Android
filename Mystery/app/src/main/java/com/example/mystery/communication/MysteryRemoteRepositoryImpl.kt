package com.example.mystery.communication

import retrofit2.Response
import javax.inject.Inject

class MysteryRemoteRepositoryImpl @Inject constructor(private val mysteryAPI: MysteryAPI): IMysteryRemoteRepository {


    override suspend fun getMystery(): CommunicationResult<MysteryItem> {
        val call: Response<MysteryItem> = mysteryAPI.getMystery()
        return handleApiCall(call)
    }
}