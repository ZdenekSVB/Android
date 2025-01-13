package cz.pef.project.communication

import cz.pef.project.ai.Recognition
import org.junit.Assert.*
import org.junit.Test

class DataClassesTest {

    @Test
    fun testRecognitionInitialization() {
        val recognition = Recognition("1", "Title", 0.9F)
        assertEquals("1", recognition.id)
        assertEquals("Title", recognition.title)
        assertEquals(0.9F, recognition.confidence, 0.0F)
    }

    @Test
    fun testGardenCenterResponseInitialization() {
        val feature = Feature(
            type = "Feature",
            properties = Properties("Name", "Address", "Description"),
            geometry = Geometry("Point", listOf(1.0, 2.0))
        )
        val response = GardenCenterResponse("FeatureCollection", listOf(feature))
        assertEquals("FeatureCollection", response.type)
        assertEquals(1, response.features.size)
        assertEquals("Name", response.features[0].properties.name)
    }
}
