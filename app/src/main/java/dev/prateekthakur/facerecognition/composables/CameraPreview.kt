package dev.prateekthakur.facerecognition.composables

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.prateekthakur.facerecognition.screens.home.CameraFrameAnalyzer

@Composable
fun CameraPreviewView(lensFacing: Int = CameraSelector.LENS_FACING_BACK) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraPermissionState = remember { mutableStateOf(checkCameraPermission(context)) }

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.value) {
            requestCameraPermission(context)
        }
    }

    CameraAndroidView(lifecycleOwner, lensFacing)
}


@Composable
private fun CameraAndroidView(
    lifecycleOwner: LifecycleOwner,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().apply {
                    surfaceProvider = previewView.surfaceProvider
                }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(lensFacing)
                    .build()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
                val analyzer = CameraFrameAnalyzer {

                }
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(ctx), analyzer)

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (exc: Exception) {
                    Log.e("CameraPreview", "Camera binding failed", exc)
                }

            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}


fun checkCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

fun requestCameraPermission(context: Context) {
    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(Manifest.permission.CAMERA),
        100
    )
}