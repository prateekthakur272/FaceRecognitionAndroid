package dev.prateekthakur.facerecognition.screens.home

import FaceEnrollViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vw_face_recognition_plugin.face_recognition.FaceRecognitionImageAnalyzer
import dev.prateekthakur.facerecognition.composables.CameraPreview
import dev.prateekthakur.facerecognition.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, viewModel: FaceEnrollViewModel
) {
    val context = LocalContext.current
    val image = viewModel.selectedImageBitmap

    var score by remember { mutableStateOf(0f) }
    val message by remember {
        derivedStateOf {
            if (score < 0) {
                return@derivedStateOf context.getString(R.string.face_not_found)
            }
            String.format(context.getString(R.string.matching), score * 100)
        }
    }

    if (image == null) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.no_image_selected_please_go_back_and_enroll))
        }
    } else {
        // Remember analyzer across recompositions
        val analyzer = remember(image) {
            FaceRecognitionImageAnalyzer(context = context,
                refImage = image,
                onSimilarityComputed = { similarity ->
                    score = similarity
                })
        }

        CameraPreview(analyzer = analyzer, usePrimaryCamera = false)

        Box(
            modifier = modifier
                .safeContentPadding()
                .size(72.dp)
                .aspectRatio(3 / 4f)
                .clip(
                    RoundedCornerShape(
                        corner = CornerSize(16.dp)
                    )
                )
        ) {
            Image(
                image.asImageBitmap(),
                contentDescription = stringResource(R.string.selected_image),
                contentScale = ContentScale.Crop,
            )
        }

        Box(contentAlignment = Alignment.TopEnd, modifier = modifier.fillMaxSize()) {
            Box(
                modifier = modifier
                    .padding(vertical = 50.dp, horizontal = 24.dp)
                    .clip(
                        RoundedCornerShape(
                            corner = CornerSize(16.dp)
                        )
                    )
                    .alpha(0.5f)
                    .background(Color.Black)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(message, color = Color.White)
            }
        }
    }
}