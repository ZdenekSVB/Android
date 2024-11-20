package cz.pef.project.communication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PetsAPI {

    @Headers("Content-Type: application/json")
    @GET("pet/findByStatus")
    suspend fun getAllPets(
        @Query("status") status: String = "available"
    ): Response<List<Pet>>


    @Headers("Content-Type: application/json")
    @GET("pet/{id}")
    suspend fun getPetById(@Path("id") id: Long): Response<Pet>


}