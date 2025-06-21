package dev.prateekthakur.facerecognition.screens.home

import android.util.Log
import android.util.Size
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face

class CameraFrameAnalyzer(
    val targetResolution: Size = Size(480, 640),
    val onFaceDetected: (List<Face>) -> Unit = {},
) : ImageAnalysis.Analyzer {

    override fun getDefaultTargetResolution(): Size {
        return targetResolution
    }

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


















