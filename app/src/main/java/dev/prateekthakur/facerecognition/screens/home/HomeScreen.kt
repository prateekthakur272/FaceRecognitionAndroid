package dev.prateekthakur.facerecognition.screens.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.prateekthakur.facerecognition.composables.CameraPreviewView

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    CameraPreviewView(modifier = modifier.fillMaxSize())
}

@Preview
@Composable
private fun HomeScreenPrev() {
    HomeScreen()
}


@Composable
fun CameraScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val cameraPermissionState = remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) }

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.value) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CAMERA),
                100
            )
        }
    }

    if (cameraPermissionState.value) {
        CameraPreviewView(modifier = modifier.fillMaxSize())
    } else {
        CircularProgressIndicator()
    }
}