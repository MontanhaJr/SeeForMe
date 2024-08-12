package com.montanhajr.seeforme.ui.screens

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.montanhajr.seeforme.R
import com.montanhajr.seeforme.ui.CameraScreenPreview
import com.montanhajr.seeforme.ui.TalkBackText
import com.montanhajr.seeforme.ui.viewmodels.ReadForMeViewModel
import com.montanhajr.seeforme.util.captureAndSendImage
import kotlinx.coroutines.delay

@Composable
fun ReadForMeScreen() {
    val viewModel: ReadForMeViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var isTextVisible by remember { mutableStateOf(true) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    val focusRequester = remember { FocusRequester() }
    val loadingFocusRequester = remember { FocusRequester() }

    val prompt = stringResource(id = R.string.readForMe_prompt)

    Box(modifier = Modifier.fillMaxSize()) {
        CameraScreenPreview(
            onCapture = { capture, _ ->
                imageCapture = capture
            }
        )

        if (isTextVisible) {
            LaunchedEffect(Unit) {
                delay(100) // delay for talkback focus
                focusRequester.requestFocus()
            }
            TalkBackText(
                text = stringResource(id = R.string.instruction_text),
                focusRequester = focusRequester
            )
        }

        when (uiState) {
            is ReadForMeViewModel.UiState.Initial -> {
                Log.d("CameraScreen", "Starting...")
                LaunchedEffect(Unit) {
                    focusRequester.freeFocus()
                }
            }

            is ReadForMeViewModel.UiState.Loading -> {
                LaunchedEffect(loadingFocusRequester) {
                    delay(100) // delay for talkback focus
                    loadingFocusRequester.requestFocus()
                }
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .focusRequester(loadingFocusRequester)
                        .focusable()
                        .semantics {
                            contentDescription =
                                context.getString(R.string.progress_indicator_loading)
                        })
            }

            is ReadForMeViewModel.UiState.Success -> {
                val result = (uiState as ReadForMeViewModel.UiState.Success).output
                LaunchedEffect(result, focusRequester) {
                    focusRequester.requestFocus()
                }
                TalkBackText(result, focusRequester)
            }

            is ReadForMeViewModel.UiState.Error -> {
                val error = (uiState as ReadForMeViewModel.UiState.Error).message
                LaunchedEffect(error) {
                    focusRequester.freeFocus() // Resetar o foco antes de requisitar novamente
                    focusRequester.requestFocus()
                }
                TalkBackText(error, focusRequester)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(64.dp)
        ) {
            Button(
                onClick = {
                    isTextVisible = false
                    viewModel.startLoading()
                    imageCapture?.let {
                        captureAndSendImage(it, context, viewModel, prompt)
                    }
                },
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .padding(6.dp)
                    .semantics {
                        contentDescription = context.getString(R.string.take_picture_button)
                    },
                elevation = ButtonDefaults.buttonElevation(10.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF48440)
                )
            ) { }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ReadForMeScreenPreview() {
    ReadForMeScreen()
}
