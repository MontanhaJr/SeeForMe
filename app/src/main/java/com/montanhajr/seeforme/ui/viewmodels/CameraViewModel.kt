package com.montanhajr.seeforme.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.montanhajr.seeforme.BuildConfig
import com.montanhajr.seeforme.ui.viewmodels.CameraViewModel.Constant.MODEL_NAME
import com.montanhajr.seeforme.ui.viewmodels.CameraViewModel.Constant.SIMILARITY_THRESHOLD
import com.montanhajr.seeforme.util.cosineSimilarity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {

    sealed class UiState {
        data object Initial : UiState()
        data object Loading : UiState()
        data class Success(val output: String) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = MODEL_NAME,
        apiKey = BuildConfig.apiKey
    )

    private var isFirstRequest = true
    private var lastOutput: String? = null

    fun sendPrompt(
        bitmap: Bitmap,
        prompt: String
    ) {
        if (isFirstRequest) {
            _uiState.value = UiState.Loading
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    if (shouldUpdateOutput(outputContent)) {
                        _uiState.value = UiState.Success(outputContent)
                        lastOutput = outputContent
                    }
                    isFirstRequest = false
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    private fun shouldUpdateOutput(newOutput: String): Boolean {
        lastOutput?.let {
            val similarity = it.cosineSimilarity(newOutput)
            return similarity < SIMILARITY_THRESHOLD
        }
        return true
    }

    private object Constant {
        const val MODEL_NAME = "gemini-1.5-flash"
        const val SIMILARITY_THRESHOLD = 0.5
    }
}