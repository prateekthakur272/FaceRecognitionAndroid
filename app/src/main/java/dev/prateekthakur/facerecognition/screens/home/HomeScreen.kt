package dev.prateekthakur.facerecognition.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.prateekthakur.facerecognition.composables.CameraPreviewView

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    CameraPreviewView()
}

@Preview
@Composable
private fun HomeScreenPrev() {
    HomeScreen()
}
