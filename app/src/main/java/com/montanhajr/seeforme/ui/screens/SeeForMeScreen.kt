package com.montanhajr.seeforme.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.montanhajr.seeforme.R
import com.montanhajr.seeforme.ui.CameraScreenPreview
import com.montanhajr.seeforme.ui.TalkBackText
import com.montanhajr.seeforme.ui.viewmodels.CameraViewModel
import com.montanhajr.seeforme.util.captureAndSendImage
import com.montanhajr.seeforme.util.showDebugLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SeeForMeScreen(prompt: String = "") {
    val viewModel: CameraViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val focusRequester = remember { FocusRequester() }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraScreenPreview(
            onCapture = { imageCapture, context ->
                CoroutineScope(Dispatchers.Main).launch {
                    while (true) {
                        captureAndSendImage(imageCapture, context, viewModel, prompt)
                        delay(5000)
                    }
                }
            }
        )

        when (uiState) {
            is CameraViewModel.UiState.Initial -> {
                Log.d("CameraScreen", "Starting...")
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
                    delay(100)
                }
                TalkBackText(result, focusRequester)
            }

            is CameraViewModel.UiState.Error -> {
                val error = (uiState as CameraViewModel.UiState.Error).message
                error.showDebugLog("CameraScreen")

                LaunchedEffect(error) {
                    focusRequester.requestFocus()
                }
                TalkBackText(stringResource(id = R.string.generic_error), focusRequester)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SeeForMeScreenPreview() {
    SeeForMeScreen()
}
