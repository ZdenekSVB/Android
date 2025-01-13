package cz.pef.project.di

import cz.pef.project.communication.GardenAPI
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Retrofit

class ApiModuleTest {

    @Test
    fun `test provideGardenAPI creates GardenAPI instance`() {
        val mockRetrofit = Mockito.mock(Retrofit::class.java)
        Mockito.`when`(mockRetrofit.create(GardenAPI::class.java)).thenReturn(Mockito.mock(GardenAPI::class.java))

        val gardenAPI = ApiModule.provideGardenAPI(mockRetrofit)

        assertNotNull(gardenAPI)
        Mockito.verify(mockRetrofit).create(GardenAPI::class.java)
    }
}
