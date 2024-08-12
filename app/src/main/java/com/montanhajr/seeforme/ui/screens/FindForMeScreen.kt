package com.montanhajr.seeforme.ui.screens

import android.content.Context
import java.util.Locale
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.montanhajr.seeforme.R
import com.montanhajr.seeforme.ui.CameraScreenPreview
import com.montanhajr.seeforme.ui.TalkBackText
import com.montanhajr.seeforme.ui.viewmodels.FindForMeViewModel
import kotlinx.coroutines.delay

@Composable
fun FindForMeScreen(navController: NavController = NavController(context = LocalContext.current)) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    var isTextVisible by remember { mutableStateOf(true) }
    var isRecording by remember { mutableStateOf(false) }
    var recordedText by remember { mutableStateOf("") }
    var showTextOptions by remember { mutableStateOf(false) }
    var instructionText by remember { mutableStateOf(context.getString(R.string.instruction_find_for_me_text)) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

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
                text = instructionText,
                focusRequester = focusRequester
            )
        }

        if (showTextOptions) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                LaunchedEffect(Unit) {
                    delay(100) // delay for talkback focus
                    focusRequester.requestFocus()
                }
                TalkBackText(
                    text = recordedText,
                    focusRequester
                )

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = {
                            recordedText = ""
                            showTextOptions = false
                            isRecording = true
                            startRecordingAudio(context, speechRecognizer, { newInstructionText ->
                                isTextVisible = true
                                isRecording = false
                                instructionText = newInstructionText
                            })
                            { transcription ->
                                recordedText = transcription
                                showTextOptions = true
                                isRecording = false
                                isTextVisible = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF48440)
                        )
                    ) {
                        Text(stringResource(id = R.string.record_again_button))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(onClick = {
                        val prompt = context.getString(R.string.findForMe_prompt, recordedText)
                        isRecording = false
                        imageCapture?.let {
                            val bundle = Bundle().apply {
                                putString("prompt", prompt)
                            }
                            navController.navigate(R.id.action_findFragment_to_cameraFragment, bundle)
                        }
                    }) {
                        Text(stringResource(id = R.string.start_looking_button))
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(64.dp)
            ) {
                IconButton(
                    onClick = {
                        isTextVisible = false
                        isRecording = !isRecording
                        if (isRecording) {
                            startRecordingAudio(context, speechRecognizer, { newInstructionText ->
                                isTextVisible = true
                                isRecording = false
                                instructionText = newInstructionText
                            }) { transcription ->
                                recordedText = transcription
                                showTextOptions = true
                                isTextVisible = false
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
                            contentDescription = stringResource(id = R.string.accesibility_record_button),
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFFF48440)
                        )
                    } else {
                        Icon(
                            Icons.Default.Stop,
                            contentDescription = stringResource(id = R.string.accesibility_stop_button),
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFFF48440)
                        )
                    }
                }
            }
        }
    }
}

private fun startRecordingAudio(
    context: Context,
    speechRecognizer: SpeechRecognizer,
    onInstructionUpdate: (String) -> Unit,
    onTranscriptionComplete: (String) -> Unit
) {
    val deviceLanguage = Locale.getDefault().toString()
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, deviceLanguage)
    }

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            // Update instruction text here if necessary
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
        }

        override fun onError(error: Int) {
            // Update instruction text for error
            onInstructionUpdate(context.getString(R.string.recording_error_text))
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                onTranscriptionComplete(matches[0])
            } else {
                // Update instruction text for unknown result
                onInstructionUpdate(context.getString(R.string.recording_unknown_text))
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
    FindForMeScreen()
}
