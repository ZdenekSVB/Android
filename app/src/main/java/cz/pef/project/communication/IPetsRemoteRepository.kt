package cz.pef.project.communication


interface IPetsRemoteRepository : IBaseRemoteRepository {

    suspend fun getAllPets(status: String): CommunicationResult<List<Pet>>
    suspend fun getPetById(id: Long) : CommunicationResult<Pet>
}