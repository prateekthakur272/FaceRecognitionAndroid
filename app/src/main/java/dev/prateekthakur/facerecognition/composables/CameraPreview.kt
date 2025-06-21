package dev.prateekthakur.facerecognition.composables

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.CameraSelector.LENS_FACING_FRONT
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.face.Face
import dev.prateekthakur.facerecognition.screens.home.CameraFrameAnalyzer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPreviewView(lensFacing: Int = LENS_FACING_FRONT) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraPermissionState = remember { mutableStateOf(checkCameraPermission(context)) }
    var faces by remember { mutableStateOf<List<Face>>(emptyList()) }

    val analyzer = CameraFrameAnalyzer {
        faces = it
    }

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.value) {
            requestCameraPermission(context)
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Matching face")
        })
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), contentAlignment = Alignment.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .aspectRatio(3 / 4f)
                    .clip(shape = RoundedCornerShape(16.dp))
            ) {
                CameraAndroidView(lifecycleOwner, lensFacing, analyzer = analyzer)
                FaceOverlay(
                    faces,
                    imageWidth = analyzer.targetResolution.width,
                    imageHeight = analyzer.targetResolution.height,
                    isFrontCamera = lensFacing == LENS_FACING_FRONT
                )
            }
        }
    }
}


@Composable
private fun CameraAndroidView(
    lifecycleOwner: LifecycleOwner,
    lensFacing: Int = LENS_FACING_BACK,
    analyzer: CameraFrameAnalyzer? = null,
    modifier: Modifier = Modifier
) {
    AndroidView(modifier = modifier, factory = { ctx ->
        val previewView = PreviewView(ctx).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().apply {
                surfaceProvider = previewView.surfaceProvider
            }

            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
            if (analyzer != null) {
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(ctx), analyzer)
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageAnalysis
                )
            } catch (exc: Exception) {
                Log.e("CameraPreview", "Camera binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(ctx))

        previewView
    })
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