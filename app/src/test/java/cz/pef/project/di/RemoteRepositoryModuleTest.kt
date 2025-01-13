package cz.pef.project.di

import cz.pef.project.communication.GardenAPI
import cz.pef.project.communication.GardenRemoteRepositoryImpl
import cz.pef.project.communication.IGardenRemoteRepository
import junit.framework.TestCase.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

class RemoteRepositoryModuleTest {

    @Test
    fun `test providePetsRepository returns IGardenRemoteRepository instance`() {
        val mockGardenAPI = Mockito.mock(GardenAPI::class.java)
        val repository = RemoteRepositoryModule.providePetsRepository(mockGardenAPI)

        assertNotNull(repository)
        assertTrue(repository is GardenRemoteRepositoryImpl)
    }
}
