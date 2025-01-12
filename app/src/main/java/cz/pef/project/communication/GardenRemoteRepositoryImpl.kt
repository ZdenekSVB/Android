package cz.pef.project.communication

import javax.inject.Inject

class GardenRemoteRepositoryImpl @Inject constructor(private val gardenAPI: GardenAPI) : IGardenRemoteRepository,IBaseRemoteRepository {

    // Zavoláme metodu makeApiCall pro získání všech zahrádkářských center
    override suspend fun getAllGardenCenters(): CommunicationResult<GardenCenterResponse> {
        return makeApiCall { gardenAPI.getAllGardenCenters() }
    }
}
