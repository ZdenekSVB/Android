package cz.pef.mendelu.examtemplate2024.communication


interface IUserRemoteRepository {

    suspend fun getUser(userId: Int): CommunicationResult<User>

    suspend fun getCountryByName(countryName: String): Country?

    suspend fun getCurrencyForCountry(countryName: String): String

    suspend fun getCapitalForCountry(countryName: String): String
}
