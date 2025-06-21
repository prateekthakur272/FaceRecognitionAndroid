package dev.prateekthakur.facerecognition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.prateekthakur.facerecognition.navigation.AppNavHost
import dev.prateekthakur.facerecognition.ui.theme.FaceRecognitionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FaceRecognitionTheme {
                AppNavHost()
            }
        }
    }
}
