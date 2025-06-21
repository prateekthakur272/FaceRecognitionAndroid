package dev.prateekthakur.facerecognition.screens.enroll

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FaceEnrollViewModel : ViewModel() {
    private val enrolledImageState = mutableStateOf<Bitmap?>(null)
    val enrolledImage: Bitmap? = enrolledImageState.value

    @Composable
    fun chooseImageFromGallery() {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri: Uri? ->
                uri?.let {

                }
            }
        )
        launcher.launch("image/*")
    }
}