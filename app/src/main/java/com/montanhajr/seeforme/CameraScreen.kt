package com.montanhajr.seeforme

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CameraScreen(imageUri: Uri?, openCamera: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (imageUri != null) {
            val painter = rememberAsyncImagePainter(imageUri)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Button(onClick = openCamera) {
                Text("Open Camera")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    CameraScreen(null) {}
}
