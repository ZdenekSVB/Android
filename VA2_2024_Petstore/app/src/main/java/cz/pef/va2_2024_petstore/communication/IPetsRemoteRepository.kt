package cz.pef.va2_2024_petstore.communication

import retrofit2.Response

interface IPetsRemoteRepository: IBaseRemoteRepository {
    suspend fun getAllPets(status:String) : CommunicationResult<List<Pet>>
    suspend fun getPetById(petId:Long) : CommunicationResult<Pet>


}