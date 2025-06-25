package dev.prateekthakur.facerecognition.screens.enroll

import FaceEnrollViewModel
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.prateekthakur.facerecognition.R
import dev.prateekthakur.facerecognition.navigation.AppRoutes
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import dev.prateekthakur.facerecognition.utils.extensions.Space


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceEnrollScreen(
    modifier: Modifier = Modifier,
    viewModel: FaceEnrollViewModel,
) {
    val context = LocalContext.current
    val image = viewModel.selectedImageBitmap

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri: Uri? ->
                uri?.let {
                    val inputStream = context.contentResolver.openInputStream(it)
                    inputStream?.let { stream ->
                        val bitmap = BitmapFactory.decodeStream(stream)
                        stream.close()
                        viewModel.setImage(bitmap.rotate(90f))
                    }
                }
            })

    val instructions = listOf(
        stringResource(R.string.instruction1),
        stringResource(R.string.instruction2),
        stringResource(R.string.instruction3)
    )

    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.enroll_face)) })
    }) { innerPadding ->

        if (image == null) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                Icon(Icons.Filled.Face, contentDescription = "", modifier = modifier.size(40.dp))
                Text(
                    stringResource(R.string.select_reference_image),
                    style = MaterialTheme.typography.titleLarge
                )
                8.Space

                instructions.map {
                    Text(it, style = MaterialTheme.typography.labelMedium)
                }

                8.Space

                Button(onClick = { launcher.launch("image/*") }) {
                    Text(stringResource(R.string.select_image_from_gallery))
                }
            }
        } else {

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .aspectRatio(3 / 4f)
                        .clip(RoundedCornerShape(corner = CornerSize(18.dp)))
                        .background(Color(0xFFD7D7D7))
                ) {
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = stringResource(R.string.selected_image),
                        contentScale = ContentScale.FillBounds
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    Button(
                        modifier = modifier.weight(1f),
                        onClick = {
                            AppRoutes.navController.navigate(AppRoutes.HomeScreen.route)
                        }) {
                        Text(stringResource(R.string.done))
                    }
                    6.Space
                    Button(
                        modifier = modifier.weight(1f),
                        onClick = {
                            viewModel.setImage(null)
                        }) {
                        Text(stringResource(R.string.clear))
                    }
                }
            }
        }
    }
}


    private fun Bitmap.rotate(fl: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(fl)
        }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }