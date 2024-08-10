package com.montanhajr.seeforme

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.camera.core.Preview as CameraPreview
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.montanhajr.seeforme.ui.viewmodels.CameraViewModel
import kotlinx.coroutines.delay
import java.nio.ByteBuffer

@Composable
fun CameraScreen() {
    val viewModel: CameraViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val focusRequester = remember { FocusRequester() }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            viewModel = viewModel,
            prompt = "Descreva o que você está vendo, utilize sempre a língua portuguesa na resposta, " +
                    "podendo usar termos em inglês comuns no Brasil caso necessário. A resposta deve ter no máximo 30 palavras"
        )

        // Sempre visível mas controlando a visibilidade real
        when (uiState) {
            is CameraViewModel.UiState.Initial -> {
                Log.d("CameraScreen", "Inicializando...")
            }

            is CameraViewModel.UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            is CameraViewModel.UiState.Success -> {
                val result = (uiState as CameraViewModel.UiState.Success).output
                LaunchedEffect(result) {
                    focusRequester.requestFocus()
                }
                TalkBackText(result, focusRequester)
            }

            is CameraViewModel.UiState.Error -> {
                val error = (uiState as CameraViewModel.UiState.Error).message
                LaunchedEffect(error) {
                    focusRequester.requestFocus()
                }
                TalkBackText(error, focusRequester)
            }
        }
    }
}

@Composable
fun TalkBackText(text: String, focusRequester: FocusRequester) {
    Text(
        text = text,
        modifier = Modifier
            .padding(16.dp)
            .focusRequester(focusRequester)
            .focusable()
            .semantics {
                contentDescription = text
            }
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
            .padding(16.dp)
    )
}


@Composable
fun CameraPreview(
    viewModel: CameraViewModel,
    prompt: String
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val previewView = remember {
        PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    LaunchedEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = CameraPreview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageCapture = ImageCapture.Builder().build()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()
        try {
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (exc: Exception) {
            Log.e("CameraPreview", "Use case binding failed", exc)
        }

        while (true) {
            captureAndSendImage(imageCapture, context, viewModel, prompt)
            delay(5000) // Intervalo de 5 segundos
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )
}

private fun captureAndSendImage(
    imageCapture: ImageCapture,
    context: android.content.Context,
    viewModel: CameraViewModel,
    prompt: String
) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = image.toBitmap()
                viewModel.sendPrompt(bitmap, prompt)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraPreview", "Camera error: ${exception.message}", exception)
            }
        }
    )
}

fun ImageProxy.toBitmap(): Bitmap {
    val buffer: ByteBuffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
    val rotationMatrix = Matrix().apply { postRotate(imageInfo.rotationDegrees.toFloat()) }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, rotationMatrix, true)
}


@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    CameraScreen()
}
