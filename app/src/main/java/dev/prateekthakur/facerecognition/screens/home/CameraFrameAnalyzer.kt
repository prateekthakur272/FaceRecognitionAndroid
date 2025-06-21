package dev.prateekthakur.facerecognition.screens.home

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face

class CameraFrameAnalyzer(val onFaceDetected: (List<Face>) -> Unit = {}) : ImageAnalysis.Analyzer {

    private val detector = FaceDetectionService.detector

    override fun analyze(imageProxy: ImageProxy) {
        Log.d("CameraFrameAnalyzer", "Analyzing image ${imageProxy.height}x${imageProxy.width}")
        val image = imageProxy.image

        image?.let {
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            detector.process(inputImage)
                .addOnSuccessListener {
                    Log.d("CameraFrameAnalyzer", "Detection Result: $it")
                    onFaceDetected(it)
                    imageProxy.close()
                }
                .addOnFailureListener { imageProxy.close() }
                .addOnCompleteListener { imageProxy.close() }
        }
    }
}


















