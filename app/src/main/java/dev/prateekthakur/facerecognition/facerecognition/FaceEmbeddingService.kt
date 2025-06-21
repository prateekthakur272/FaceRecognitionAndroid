package dev.prateekthakur.facerecognition.facerecognition

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class FaceEmbeddingService private constructor() {
    companion object {
        private const val MODEL_FILE = "facenet.tflite"
        private const val INPUT_IMAGE_SIZE = 160
        private const val EMBEDDING_SIZE = 128

        private lateinit var interpreter: Interpreter
        private var isInitialized = false

        fun init(context: Context) {
            if (isInitialized) return

            val assetFileDescriptor = context.assets.openFd(MODEL_FILE)
            val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val fileChannel = fileInputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            val modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)

            interpreter = Interpreter(modelBuffer)
            isInitialized = true
        }

        fun getEmbedding(faceBitmap: Bitmap): FloatArray {
            checkInit()

            val resized = Bitmap.createScaledBitmap(faceBitmap, INPUT_IMAGE_SIZE, INPUT_IMAGE_SIZE, true)
            val input = convertBitmapToBuffer(resized)

            val embedding = Array(1) { FloatArray(EMBEDDING_SIZE) }
            interpreter.run(input, embedding)

            return l2Normalize(embedding[0])
        }

        fun cropFace(bitmap: Bitmap, faceRect: Rect): Bitmap {
            val safeRect = Rect(
                faceRect.left.coerceAtLeast(0),
                faceRect.top.coerceAtLeast(0),
                faceRect.right.coerceAtMost(bitmap.width),
                faceRect.bottom.coerceAtMost(bitmap.height)
            )
            return Bitmap.createBitmap(bitmap, safeRect.left, safeRect.top, safeRect.width(), safeRect.height())
        }

        private fun convertBitmapToBuffer(bitmap: Bitmap): ByteBuffer {
            val intValues = IntArray(INPUT_IMAGE_SIZE * INPUT_IMAGE_SIZE)
            bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

            val floatValues = FloatArray(INPUT_IMAGE_SIZE * INPUT_IMAGE_SIZE * 3)
            var index = 0
            for (pixel in intValues) {
                floatValues[index++] = (pixel shr 16 and 0xFF).toFloat() // R
                floatValues[index++] = (pixel shr 8 and 0xFF).toFloat()  // G
                floatValues[index++] = (pixel and 0xFF).toFloat()        // B
            }

            val mean = floatValues.average().toFloat()
            var std = sqrt(floatValues.fold(0f) { acc, v -> acc + (v - mean).pow(2) } / floatValues.size)
            std = max(std, 1f / sqrt(floatValues.size.toFloat()))

            for (i in floatValues.indices) {
                floatValues[i] = (floatValues[i] - mean) / std
            }

            val byteBuffer = ByteBuffer.allocateDirect(4 * floatValues.size)
            byteBuffer.order(ByteOrder.nativeOrder())
            floatValues.forEach { byteBuffer.putFloat(it) }
            byteBuffer.rewind()

            return byteBuffer
        }

        private fun l2Normalize(embedding: FloatArray): FloatArray {
            val norm = sqrt(embedding.fold(0f) { acc, v -> acc + v * v })
            return embedding.map { it / norm }.toFloatArray()
        }

        private fun checkInit() {
            if (!isInitialized) {
                throw IllegalStateException("FaceEmbeddingService not initialized. Call FaceEmbeddingService.init(context) first.")
            }
        }
    }
}