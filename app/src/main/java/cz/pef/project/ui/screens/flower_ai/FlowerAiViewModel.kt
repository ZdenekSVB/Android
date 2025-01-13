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
import cz.pef.project.R
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
    application: Application, private val userDao: UserDao, val dataStoreManager: DataStoreManager
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
        context.getString(R.string.apple_apple_scab),
        context.getString(R.string.apple_black_rot),
        context.getString(R.string.apple_cedar_apple_rust),
        context.getString(R.string.apple_healthy),
        context.getString(R.string.blueberry_healthy),
        context.getString(R.string.cherry_including_sour_powdery_mildew),
        context.getString(R.string.cherry_including_sour_healthy),
        context.getString(R.string.corn_maize_cercospora_leaf_spot_gray_leaf_spot),
        context.getString(R.string.corn_maize_common_rust),
        context.getString(R.string.corn_maize_northern_leaf_blight),
        context.getString(R.string.corn_maize_healthy),
        context.getString(R.string.grape_black_rot),
        context.getString(R.string.grape_esca_black_measles),
        context.getString(R.string.grape_leaf_blight_isariopsis_leaf_spot),
        context.getString(R.string.grape_healthy),
        context.getString(R.string.orange_haunglongbing_citrus_greening),
        context.getString(R.string.peach_bacterial_spot),
        context.getString(R.string.peach_healthy),
        context.getString(R.string.pepper_bell_bacterial_spot),
        context.getString(R.string.pepper_bell_healthy),
        context.getString(R.string.potato_early_blight),
        context.getString(R.string.potato_late_blight),
        context.getString(R.string.potato_healthy),
        context.getString(R.string.raspberry_healthy),
        context.getString(R.string.soybean_healthy),
        context.getString(R.string.squash_powdery_mildew),
        context.getString(R.string.strawberry_leaf_scorch),
        context.getString(R.string.strawberry_healthy),
        context.getString(R.string.tomato_bacterial_spot),
        context.getString(R.string.tomato_early_blight),
        context.getString(R.string.tomato_late_blight),
        context.getString(R.string.tomato_leaf_mold),
        context.getString(R.string.tomato_septoria_leaf_spot),
        context.getString(R.string.tomato_spider_mites_two_spotted_spider_mite),
        context.getString(R.string.tomato_target_spot),
        context.getString(R.string.tomato_tomato_yellow_leaf_curl_virus),
        context.getString(R.string.tomato_tomato_mosaic_virus),
        context.getString(R.string.tomato_healthy)
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
                    uiState.value = uiState.value.copy(analysisResult = results.joinToString("\n") {
                        val confidencePercentage = it.confidence * 100
                        "${it.title} (Confidence: %.2f%%)".format(confidencePercentage)
                    })

                } else {
                    uiState.value =
                        uiState.value.copy(analysisResult = context.getString(R.string.no_results_found))
                }

                // Uložení do databáze
                saveResultsToDatabase(plantId, results)
            }
        }
    }

    private suspend fun saveResultsToDatabase(plantId: Int, results: List<Recognition>) {
        val bestResult = results.firstOrNull()
        if (bestResult != null) {
            val condition = bestResult.title.substringBefore(":").trim()
            val description = bestResult.title.substringAfter(":").trim()

            val plant = userDao.getPlantById(plantId)
            if (plant != null) {
                plant.lastCondition = condition
                userDao.updatePlant(plant)
            } else {
                Log.e("FlowerAiViewModel", "Plant with ID $plantId not found")
            }

            val entity = ResultEntity(
                plantId = plantId, condition = condition, description = description
            )
            userDao.insertResult(entity)
        }
    }

    private fun getSortedResult(outputArray: FloatArray): List<Recognition> {
        val pq = PriorityQueue(3,
            Comparator<Recognition> { o1, o2 -> o2.confidence.compareTo(o1.confidence) })

        outputArray.forEachIndexed { index, confidence ->
            if (confidence >= 0.4f) {
                pq.add(
                    Recognition(
                        id = index.toString(),
                        title = labels.getOrNull(index) ?: context.getString(R.string.unknown),
                        confidence = confidence
                    )
                )
            }
        }

        return List(minOf(pq.size, 3)) { pq.poll() }
    }

    private fun preprocessImage(uri: Uri): TensorImage {
        val originalBitmap = loadBitmapFromUri(uri, context)
        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 224, 224, true)

        val intValues = IntArray(224 * 224)
        resizedBitmap.getPixels(intValues, 0, 224, 0, 0, 224, 224)

        val bufferSize = 224 * 224 * 3 * 4
        val byteBuffer = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())

        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val pixel = intValues[y * 224 + x]
                byteBuffer.putFloat(((pixel shr 16 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((pixel shr 8 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((pixel and 0xFF) / 255.0f))
            }
        }

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resizedBitmap)
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