package cz.pef.mendelu.examtemplate2024.communication

import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UserRemoteRepositoryImpl @Inject constructor(private val userAPI: UserAPI) :
    IUserRemoteRepository, IBaseRemoteRepository {

    override suspend fun getUser(number: Int): CommunicationResult<User> {
        return makeApiCall { userAPI.getUserByNumber(number) }
    }

    override suspend fun getCountryByName(countryName: String): Country? {
        val result = makeApiCall { userAPI.getAllCountries() }
        return when (result) {
            is CommunicationResult.Success -> result.data.find { it.name.equals(countryName) }
            else -> null
        }
    }


    override suspend  fun getCurrencyForCountry(countryName: String): String {
        val country = getCountryByNameBlocking(countryName)
        return country?.currencies?.values?.firstOrNull()?.name ?: "Unknown"
    }

    override suspend fun getCapitalForCountry(countryName: String): String {
        val country = getCountryByNameBlocking(countryName)
        return country?.capital?.firstOrNull() ?: "Unknown"
    }

    // Pomocná funkce pro synchronní volání `getCountryByName`
    private suspend fun getCountryByNameBlocking(countryName: String): Country? {
        return runBlocking {
            getCountryByName(countryName)
        }
    }
}
