package dev.prateekthakur.facerecognition.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.google.mlkit.vision.face.Face

@Composable
fun FaceOverlay(
    faces: List<Face>,
    imageWidth: Int,
    imageHeight: Int,
    isFrontCamera: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val scaleX = size.width / imageWidth.toFloat()
        val scaleY = size.height / imageHeight.toFloat()

        for (face in faces) {
            val box = face.boundingBox

            val left = box.left * scaleX
            val top = box.top * scaleY
            val right = box.right * scaleX
            val bottom = box.bottom * scaleY

            val rectLeft = if (isFrontCamera) size.width - right else left
            val rectRight = if (isFrontCamera) size.width - left else right

            drawRoundRect(
                color = Color.Green,
                topLeft = Offset(rectLeft, top),
                size = Size(rectRight - rectLeft, bottom - top),
                style = Stroke(width = 8f),
                cornerRadius = CornerRadius(20f, 20f)
            )
        }
    }
}