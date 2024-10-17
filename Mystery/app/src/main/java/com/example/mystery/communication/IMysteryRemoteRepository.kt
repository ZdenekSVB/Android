package com.example.mystery.communication

interface IMysteryRemoteRepository: IBaseRemoteRepository {

    suspend fun getMystery() : CommunicationResult<MysteryItem>
}