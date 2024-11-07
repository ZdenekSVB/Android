package cz.pef.mendelu.examtemplate2024.communication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UserAPI {

    @Headers("Content-Type: application/json")
    @GET("user_{userNumber}.json")
    suspend fun getUserByNumber(@Path("userNumber") userNumber: Int): Response<User>

    @Headers("Content-Type: application/json")
    @GET("allcountries.json")
    suspend fun getAllCountries(): Response<List<Country>>

}
