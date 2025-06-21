package dev.prateekthakur.facerecognition.screens.enroll

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import dev.prateekthakur.facerecognition.R
import dev.prateekthakur.facerecognition.navigation.AppRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceEnrollScreen(
    modifier: Modifier = Modifier, onImageSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    var image by remember { mutableStateOf<ImageBitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri: Uri? ->
                uri?.let {
                    onImageSelected(it)
                    context.contentResolver.openInputStream(uri)
                    val bitmap =
                        BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
                    image = bitmap.asImageBitmap()
                }
            })

    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.enroll_face)) })
    }) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .aspectRatio(3 / 4f)
                    .clip(shape = RoundedCornerShape(corner = CornerSize(18.dp)))
                    .background(color = Color(0xFFD7D7D7))
            ) {
                if (image != null) {
                    Image(
                        bitmap = image!!,
                        contentDescription = stringResource(R.string.selected_image),
                        contentScale = ContentScale.FillBounds
                    )
                } else {
                    Text(stringResource(R.string.no_face_enrolled))
                }
            }
            Spacer(modifier = modifier.height(12.dp))
            Button(modifier = modifier.fillMaxWidth(),
                onClick = {
                    launcher.launch("image/*")
                }) {
                Text(stringResource(R.string.select_image_from_gallery))
            }

            Spacer(modifier = modifier.height(12.dp))
            if (image != null) {
                Button(modifier = modifier.fillMaxWidth(), onClick = {
                    AppRoutes.navController.navigate(AppRoutes.HomeScreen.route)
                }) {
                    Text(stringResource(R.string.check_face))
                }
            }
        }
    }
}