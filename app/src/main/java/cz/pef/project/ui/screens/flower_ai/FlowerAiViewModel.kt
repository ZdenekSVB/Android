package cz.pef.project.ui.screens.flower_ai


import android.app.Application
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import cz.pef.project.DB.ResultEntity
import cz.pef.project.ai.Recognition
import cz.pef.project.dao.UserDao
import cz.pef.project.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.PriorityQueue
import javax.inject.Inject

/*

Tvůrce modelu:
obeshor Yannick Serge Obam
odkaz:
https://github.com/obeshor/Plant-Diseases-Detector/blob/master/GreenDoctor/app/src/main/assets/plant_disease_model.tflite

 */

@HiltViewModel
class FlowerAiViewModel @Inject constructor(
    application: Application,
    private val userDao: UserDao,
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val context = application
    val uiState = mutableStateOf(FlowerAiUiState())

    private lateinit var interpreter: Interpreter
    private lateinit var interpreterDeferred: Deferred<Interpreter>

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme

    init {
        viewModelScope.launch {
            observeThemePreference()
            interpreterDeferred = async { loadModelFile("plant_disease_model.tflite") }
            interpreter = interpreterDeferred.await()
        }
    }

    private val labels = listOf(
        "Apple Apple Scab: https://en.wikipedia.org/wiki/Apple_scab",
        "Apple Black Rot: https://en.wikipedia.org/wiki/Apple_black_rot",
        "Apple Cedar Apple Rust: https://en.wikipedia.org/wiki/Cedar_apple_rust",
        "Apple Healthy: https://en.wikipedia.org/wiki/Apple",
        "Blueberry Healthy: https://en.wikipedia.org/wiki/Blueberry",
        "Cherry Including Sour Powdery Mildew: https://en.wikipedia.org/wiki/Powdery_mildew",
        "Cherry Including Sour Healthy: https://en.wikipedia.org/wiki/Cherry",
        "Corn Maize Cercospora Leaf Spot Gray Leaf Spot: https://en.wikipedia.org/wiki/Cercospora_leaf_spot",
        "Corn Maize Common Rust: https://en.wikipedia.org/wiki/Puccinia_sorghi",
        "Corn Maize Northern Leaf Blight: https://en.wikipedia.org/wiki/Northern_leaf_blight",
        "Corn Maize Healthy: https://en.wikipedia.org/wiki/Maize",
        "Grape Black Rot: https://en.wikipedia.org/wiki/Grape_black_rot",
        "Grape Esca Black Measles: https://en.wikipedia.org/wiki/Esca_(grapevine_disease)",
        "Grape Leaf Blight Isariopsis Leaf Spot: https://en.wikipedia.org/wiki/Leaf_spot",
        "Grape Healthy: https://en.wikipedia.org/wiki/Grape",
        "Orange Haunglongbing Citrus Greening: https://en.wikipedia.org/wiki/Citrus_greening",
        "Peach Bacterial Spot: https://en.wikipedia.org/wiki/Bacterial_spot",
        "Peach Healthy: https://en.wikipedia.org/wiki/Peach",
        "Pepper Bell Bacterial Spot: https://en.wikipedia.org/wiki/Bacterial_spot",
        "Pepper Bell Healthy: https://en.wikipedia.org/wiki/Pepper",
        "Potato Early Blight: https://en.wikipedia.org/wiki/Early_blight_of_potato",
        "Potato Late Blight: https://en.wikipedia.org/wiki/Late_blight",
        "Potato Healthy: https://en.wikipedia.org/wiki/Potato",
        "Raspberry Healthy: https://en.wikipedia.org/wiki/Raspberry",
        "Soybean Healthy: https://en.wikipedia.org/wiki/Soybean",
        "Squash Powdery Mildew: https://en.wikipedia.org/wiki/Powdery_mildew",
        "Strawberry Leaf Scorch: https://en.wikipedia.org/wiki/Strawberry_leaf_scorch",
        "Strawberry Healthy: https://en.wikipedia.org/wiki/Strawberry",
        "Tomato Bacterial Spot: https://en.wikipedia.org/wiki/Bacterial_spot",
        "Tomato Early Blight: https://en.wikipedia.org/wiki/Early_blight_of_tomato",
        "Tomato Late Blight: https://en.wikipedia.org/wiki/Late_blight_of_tomato",
        "Tomato Leaf Mold: https://en.wikipedia.org/wiki/Leaf_mold_of_tomato",
        "Tomato Septoria Leaf Spot: https://en.wikipedia.org/wiki/Septoria_leaf_spot",
        "Tomato Spider Mites Two Spotted Spider Mite: https://en.wikipedia.org/wiki/Two-spotted_spider_mite",
        "Tomato Target Spot: https://en.wikipedia.org/wiki/Target_spot_(tomato)",
        "Tomato Tomato Yellow Leaf Curl Virus: https://en.wikipedia.org/wiki/Tomato_yellow_leaf_curl_virus",
        "Tomato Tomato Mosaic Virus: https://en.wikipedia.org/wiki/Tomato_mosaic_virus",
        "Tomato Healthy: https://en.wikipedia.org/wiki/Tomato"
    )


    private fun loadModelFile(modelPath: String): Interpreter {
        return try {
            val assetFileDescriptor = context.assets.openFd(modelPath)
            val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            val modelByteBuffer =
                fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            Interpreter(modelByteBuffer)
        } catch (e: Exception) {
            throw RuntimeException("Error loading model file $modelPath", e)
        }
    }


    fun selectImage(uri: Uri) {
        uiState.value = uiState.value.copy(selectedImageUri = uri)
    }

    fun analyzeImage(plantId: Int) {
        viewModelScope.launch {
            if (!::interpreter.isInitialized) {
                interpreter = interpreterDeferred.await()
            }
            uiState.value.selectedImageUri?.let { uri ->
                val tensorImage = preprocessImage(uri)
                val outputBuffer =
                    TensorBuffer.createFixedSize(intArrayOf(1, labels.size), DataType.FLOAT32)
                interpreter.run(tensorImage.buffer, outputBuffer.buffer.rewind())

                val outputArray = outputBuffer.floatArray
                val results = getSortedResult(outputArray)

                if (results.isNotEmpty()) {
                    uiState.value = uiState.value.copy(
                        analysisResult = results.joinToString("\n") {
                            val confidencePercentage = it.confidence * 100
                            "${it.title} (Confidence: %.2f%%)".format(confidencePercentage)

                        }
                    )

                } else {
                    uiState.value = uiState.value.copy(analysisResult = "No results found.")
                }


                // Uložení do databáze
                saveResultsToDatabase(plantId, results)
            }
        }
    }

    private suspend fun saveResultsToDatabase(plantId: Int, results: List<Recognition>) {
        // Předpokládáme, že první výsledek z analýzy je nejrelevantnější
        val bestResult = results.firstOrNull()
        if (bestResult != null) {
            val condition = bestResult.title.substringBefore(":").trim() // Název
            val description = bestResult.title.substringAfter(":").trim() // URL nebo popis

            // Aktualizace PlantEntity
            val plant = userDao.getPlantById(plantId) // Získání kytky podle ID
            if (plant != null) {
                plant.lastCondition = condition
                userDao.updatePlant(plant) // Uložení změn do databáze
            } else {
                Log.e("FlowerAiViewModel", "Plant with ID $plantId not found")
            }

            // Volitelně: Uložit podrobný výsledek analýzy do jiné tabulky, např. ResultEntity
            val entity = ResultEntity(
                plantId = plantId,
                condition = condition,
                description = description
            )
            userDao.insertResult(entity)
        }
    }


    private fun getSortedResult(outputArray: FloatArray): List<Recognition> {
        val pq = PriorityQueue(
            3, // Maximální počet výsledků
            Comparator<Recognition> { o1, o2 -> o2.confidence.compareTo(o1.confidence) }
        )

        outputArray.forEachIndexed { index, confidence ->
            if (confidence >= 0.4f) { // THRESHOLD
                pq.add(
                    Recognition(
                        id = index.toString(),
                        title = labels.getOrNull(index) ?: "Unknown",
                        confidence = confidence
                    )
                )
            }
        }

        return List(minOf(pq.size, 3)) { pq.poll() } // Vrací 3 nejlepší výsledky
    }

    private fun preprocessImage(uri: Uri): TensorImage {
        val originalBitmap = loadBitmapFromUri(uri, context)
        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 224, 224, true)

        val intValues = IntArray(224 * 224)
        resizedBitmap.getPixels(intValues, 0, 224, 0, 0, 224, 224)

        val bufferSize = 224 * 224 * 3 * 4 // 224x224, RGB (3 kanály), 4 bajty na kanál
        val byteBuffer = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())

        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val pixel = intValues[y * 224 + x]
                byteBuffer.putFloat(((pixel shr 16 and 0xFF) / 255.0f)) // Červený kanál
                byteBuffer.putFloat(((pixel shr 8 and 0xFF) / 255.0f))  // Zelený kanál
                byteBuffer.putFloat(((pixel and 0xFF) / 255.0f))        // Modrý kanál
            }
        }

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resizedBitmap) // Tímto se buffer připojí k TensorImage
        return tensorImage
    }


    private fun loadBitmapFromUri(uri: Uri, context: Context): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun observeThemePreference() {
        viewModelScope.launch {
            dataStoreManager.darkModeFlow.collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
    }
}
