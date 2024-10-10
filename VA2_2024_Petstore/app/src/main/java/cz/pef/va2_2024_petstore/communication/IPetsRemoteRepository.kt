package cz.pef.va2_2024_petstore.communication

import retrofit2.Response

interface IPetsRemoteRepository {
    suspend fun getAllPets(status:String) : CommunicationResult<List<Pet>>


}