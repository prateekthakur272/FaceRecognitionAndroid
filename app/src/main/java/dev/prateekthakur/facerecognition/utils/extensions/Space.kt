package dev.prateekthakur.facerecognition.utils.extensions

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


val Int.Space: Unit
    @Composable
    get() {
        Spacer(modifier = Modifier.padding(all = this.dp))
    }