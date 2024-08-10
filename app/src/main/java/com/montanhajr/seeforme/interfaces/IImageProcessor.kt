package com.montanhajr.seeforme.interfaces

import android.graphics.Bitmap

interface IImageProcessor {
    fun sendPrompt(bitmap: Bitmap, prompt: String)
}
