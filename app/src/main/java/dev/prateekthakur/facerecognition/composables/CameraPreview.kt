package dev.prateekthakur.facerecognition.composables

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.core.ImageAnalysis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.prateekthakur.facerecognition.views.CameraView

@Composable
fun CameraPreview(usePrimaryCamera: Boolean = true, analyzer: ImageAnalysis.Analyzer? = null) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val hasPermission = remember { mutableStateOf(checkCameraPermission(context)) }

    LaunchedEffect(Unit) {
        if (!hasPermission.value) {
            requestCameraPermission(context)
        }
    }

    LaunchedEffect(Unit) {
        if (!hasPermission.value) {
            kotlinx.coroutines.delay(500)
            hasPermission.value = checkCameraPermission(context)
        }
    }

    if (hasPermission.value) {
        AndroidView(factory = {
            CameraView(
                context = context,
                lifecycleOwner = lifecycleOwner,
                usePrimaryCamera = usePrimaryCamera,
                analyzer = analyzer
            ).getView()
        })
    }
}

fun checkCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

fun requestCameraPermission(context: Context) {
    ActivityCompat.requestPermissions(
        context as Activity, arrayOf(Manifest.permission.CAMERA), 100
    )
}