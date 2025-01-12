package cz.pef.project.communication


interface IGardenRemoteRepository {

    suspend fun getAllGardenCenters(): CommunicationResult<GardenCenterResponse>
}