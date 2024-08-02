package com.montanhajr.seeforme.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import coil.compose.rememberAsyncImagePainter
import com.montanhajr.seeforme.CameraScreen
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class CameraFragment : Fragment() {

    private var _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> = _imageUri
    private var photoURI: Uri? = null

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && photoURI != null) {
            _imageUri.value = photoURI
        } else {
            // Remova o arquivo criado se a captura não foi bem-sucedida
            photoURI?.let { uri ->
                val file = File(uri.path!!)
                file.delete()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                CameraScreen(imageUri.value) {
                    openCamera()
                }
            }
        }
    }

    private fun openCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            // Handle error
            null
        }

        // Continue only if the File was successfully created
        photoFile?.also {
            photoURI = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                it
            )
            takePicture.launch(photoURI)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        // Certifique-se de que o diretório existe
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }
}

@Composable
fun CameraScreen(imageUri: Uri?, onOpenCamera: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = null,
                modifier = Modifier.padding(16.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Button(onClick = onOpenCamera) {
                Text(text = "Open Camera")
            }
        }
    }
}
