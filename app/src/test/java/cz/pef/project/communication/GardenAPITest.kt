package cz.pef.project.communication

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.runBlocking

class GardenAPITest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var gardenAPI: GardenAPI

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        gardenAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GardenAPI::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetAllGardenCenters() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                  "type": "FeatureCollection",
                  "features": []
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        val response = gardenAPI.getAllGardenCenters()
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())
        assertEquals("FeatureCollection", response.body()?.type)
    }
}
