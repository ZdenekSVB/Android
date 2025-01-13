package cz.pef.project.communication

import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

class GardenRemoteRepositoryImplTest {

    private val mockGardenAPI = mock(GardenAPI::class.java)
    private val repository = GardenRemoteRepositoryImpl(mockGardenAPI)

    @Test
    fun testGetAllGardenCenters() = runBlocking {
        val expectedResponse = Response.success(
            GardenCenterResponse("FeatureCollection", emptyList())
        )

        `when`(mockGardenAPI.getAllGardenCenters()).thenReturn(expectedResponse)

        val result = repository.getAllGardenCenters()
        if (result is CommunicationResult.Success) {
            assertEquals("FeatureCollection", result.data.type)
        } else {
            fail("Expected CommunicationResult.Success but got $result")
        }
    }




}
