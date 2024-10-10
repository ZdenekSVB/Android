package cz.pef.va2_2024_petstore.communication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PetsAPI {
    //Query pokud je za otaznikem
    @Headers("Content-Type: application/json")
    @GET("pet/findByStatus")
    suspend fun getAllPets(
        @Query("status")status:String = "available"
    ): Response<List<Pet>>
    //Path pokud je za lomitkem
    @Headers("Content-Type: application/json")
    @GET("pet/{petId}")
    suspend fun getPetById(
        @Path("petId")petId:Long
    ): Response<Pet>
}