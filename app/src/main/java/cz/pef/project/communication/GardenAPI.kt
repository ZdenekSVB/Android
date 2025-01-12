package cz.pef.project.communication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers



interface GardenAPI {

    // Hlava pro specifikování formátu JSON
    @Headers("Content-Type: application/json")
    @GET("gardencenters.json")
    suspend fun getAllGardenCenters(): Response<GardenCenterResponse>
}
