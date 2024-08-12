package com.montanhajr.seeforme.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.montanhajr.seeforme.BuildConfig
import com.montanhajr.seeforme.interfaces.IImageProcessor
import com.montanhajr.seeforme.ui.viewmodels.ReadForMeViewModel.Constant.MODEL_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReadForMeViewModel : ViewModel(), IImageProcessor {

    sealed class UiState {
        data object Initial : UiState()
        data object Loading : UiState()
        data class Success(val output: String) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun startLoading() {
        _uiState.value = UiState.Loading
    }

    private val generativeModel = GenerativeModel(
        modelName = MODEL_NAME,
        apiKey = BuildConfig.apiKey
    )

    override fun sendPrompt(
        bitmap: Bitmap,
        prompt: String
    ) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    private object Constant {
        const val MODEL_NAME = "gemini-1.5-flash"
    }
}