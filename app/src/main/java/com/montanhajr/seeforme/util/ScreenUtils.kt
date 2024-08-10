package com.montanhajr.seeforme.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import com.montanhajr.seeforme.interfaces.IImageProcessor
import java.nio.ByteBuffer


fun captureAndSendImage(
    imageCapture: ImageCapture,
    context: Context,
    processor: IImageProcessor,
    prompt: String
) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = image.toBitmap()
                processor.sendPrompt(bitmap, prompt)
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