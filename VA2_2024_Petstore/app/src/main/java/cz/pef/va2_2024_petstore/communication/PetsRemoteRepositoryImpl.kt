package cz.pef.va2_2024_petstore.communication

import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PetsRemoteRepositoryImpl @Inject constructor(private val petsAPI: PetsAPI): IPetsRemoteRepository {

    override suspend fun getAllPets(status: String): CommunicationResult<List<Pet>> {
        return try {
        val call: Response<List<Pet>> = petsAPI.getAllPets(status)
        handleApiCall(call)
    }catch (ex: UnknownHostException) {
            CommunicationResult.Exception(ex)
        } catch (ex: SocketTimeoutException) {
            CommunicationResult.ConnectionError()
        } catch (ex: Exception) {
            CommunicationResult.Exception(ex)
        }
    }

    override suspend fun getPetById(petId: Long): CommunicationResult<Pet> {
        val call: Response<Pet> = petsAPI.getPetById(petId)
        return handleApiCall(call)
    }
}