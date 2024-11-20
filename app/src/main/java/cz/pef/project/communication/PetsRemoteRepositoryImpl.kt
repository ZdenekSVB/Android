package cz.pef.project.communication

import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PetsRemoteRepositoryImpl @Inject constructor(private val petsAPI: PetsAPI) : IPetsRemoteRepository {

    override suspend fun getAllPets(status: String): CommunicationResult<List<Pet>> {
        try {
            val call: Response<List<Pet>> = petsAPI.getAllPets(status)
            return handleResponse(call)
        }  catch (ex: UnknownHostException){
            return CommunicationResult.Exception(ex)
        } catch (ex: SocketTimeoutException){
            return CommunicationResult.ConnectionError()
        } catch (ex: Exception) {
            return CommunicationResult.Exception(ex)
        }

    }

    override suspend fun getPetById(id: Long): CommunicationResult<Pet> {
        return makeApiCall { petsAPI.getPetById(id) }

    }
}