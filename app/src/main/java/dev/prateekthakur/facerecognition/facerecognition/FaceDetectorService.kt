package dev.prateekthakur.facerecognition.facerecognition

import android.media.Image
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectionService {

    companion object {
        val detector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()
        )

        fun detectFaces(image: Image, rotationDegrees: Int, onFaceDetected: (List<Face>) -> Unit) {
            val inputImage = InputImage.fromMediaImage(image, rotationDegrees)
            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    onFaceDetected(faces)
                }
                .addOnFailureListener {
                    onFaceDetected(emptyList())
                }.addOnCompleteListener {
                    onFaceDetected(emptyList())
                }
        }
    }
}