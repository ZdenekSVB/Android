package cz.pef.project.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecognitionTest {

    @Test
    fun `test default values`() {
        val recognition = Recognition()

        assertEquals("", recognition.id)
        assertEquals("", recognition.title)
        assertEquals(0F, recognition.confidence)
    }

    @Test
    fun `test custom values`() {
        val recognition = Recognition(
            id = "123",
            title = "Test Title",
            confidence = 0.9F
        )

        assertEquals("123", recognition.id)
        assertEquals("Test Title", recognition.title)
        assertTrue(recognition.confidence == 0.9F)
    }
}
