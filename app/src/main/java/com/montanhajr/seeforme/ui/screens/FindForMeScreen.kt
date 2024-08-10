package com.montanhajr.seeforme.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.montanhajr.seeforme.ui.CameraScreenPreview
import com.montanhajr.seeforme.ui.TalkBackText
import com.montanhajr.seeforme.ui.viewmodels.FindForMeViewModel
import com.montanhajr.seeforme.ui.viewmodels.ReadForMeViewModel
import com.montanhajr.seeforme.util.captureAndSendImage

@Composable
fun FindForMeScreen() {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    var isRecording by remember { mutableStateOf(false) }
    var recordedText by remember { mutableStateOf("") }
    var showTextOptions by remember { mutableStateOf(false) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraScreenPreview(
            onCapture = { capture, _ ->
                imageCapture = capture
            }
        )

        if (showTextOptions) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                TalkBackText(
                    text = recordedText,
                    focusRequester
                )

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Button(onClick = { /* Lógica para regravar */ }) {
                        Text("Gravar novamente")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(onClick = {
                        isRecording = false
                        startSendingImages(imageCapture, context)
                    }) {
                        Text("Está correto")
                    }
                }
            }
        } else {
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(64.dp)) {
                IconButton(
                    onClick = {
                        isRecording = !isRecording
                        if (isRecording) {
                            startRecordingAudio(context) { transcription ->
                                recordedText = transcription
                                showTextOptions = true
                            }
                        } else {
                            stopRecording(speechRecognizer)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(80.dp)
                        .background(Color.White, CircleShape)
                    ) {
                    if (!isRecording) {
                        Icon(
                            Icons.Default.Mic,
                            contentDescription = "Gravar",
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFFF48440)
                        )
                    } else {
                        Icon(
                            Icons.Default.Stop,
                            contentDescription = "Gravando",
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFFF48440)
                        )
                    }
                }
            }
        }
    }
}

private fun startSendingImages(imageCapture: ImageCapture?, context: Context) {
    imageCapture?.let {
        // Aqui inicia o loop de envio de imagens a cada 5 segundos
        // Semelhante ao que foi feito anteriormente
    }
}

private fun startRecordingAudio(context: Context, onTranscriptionComplete: (String) -> Unit) {
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
    }

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Toast.makeText(context, "Iniciando gravação...", Toast.LENGTH_SHORT).show()
        }

        override fun onBeginningOfSpeech() {
            // Called when the user starts speaking
        }

        override fun onRmsChanged(rmsdB: Float) {
            // Called when the volume level changes
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            // Called when more sound is received
        }

        override fun onEndOfSpeech() {
            // Called when the user stops speaking
            Toast.makeText(context, "Processando áudio...", Toast.LENGTH_SHORT).show()
        }

        override fun onError(error: Int) {
            Toast.makeText(context, "Erro na gravação: $error", Toast.LENGTH_SHORT).show()
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                onTranscriptionComplete(matches[0]) // Use the first result
            } else {
                Toast.makeText(context, "Nenhum texto reconhecido", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            // Called with partial speech results
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            // A generic event occurred
        }
    })

    speechRecognizer.startListening(intent)
}

private fun stopRecording(speechRecognizer: SpeechRecognizer) {
    speechRecognizer.stopListening()
}


@Preview(showBackground = true)
@Composable
fun FindForMeScreenPreview() {
    ReadForMeScreen()
}
