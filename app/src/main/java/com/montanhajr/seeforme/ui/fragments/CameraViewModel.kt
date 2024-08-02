package com.montanhajr.seeforme.ui.fragments

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {

    private val _capturedImageUri = mutableStateOf<Uri?>(null)
    val capturedImageUri: MutableState<Uri?> = _capturedImageUri

    val capturedImage = MutableLiveData<Bitmap?>()
}