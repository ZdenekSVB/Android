package cz.pef.va2_2024_petstore.communication

import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PetsRemoteRepositoryImpl @Inject constructor(private val petsAPI: PetsAPI): IPetsRemoteRepository {

    override suspend fun getAllPets(status: String): CommunicationResult<List<Pet>> {
        try {
            val call: Response<List<Pet>> = petsAPI.getAllPets(status)
            if (call.isSuccessful){
                call.body()?.let {return CommunicationResult.Success(call.body()!!) }?:run {
                    return CommunicationResult.Error(CommunicationError(call.code(),call.message()))
                }

            }else{
                return CommunicationResult.Error(CommunicationError(call.code(),call.message()))
            }
        }
        catch (ex:UnknownHostException){
            return CommunicationResult.Exception(ex)
        } catch (ex: SocketTimeoutException){
            return CommunicationResult.ConnectionError()
        }catch (ex: Exception){
            return CommunicationResult.Exception(ex)
        }


    }
}