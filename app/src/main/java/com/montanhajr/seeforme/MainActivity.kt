package com.montanhajr.seeforme

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest.permission.CAMERA
import android.Manifest.permission.RECORD_AUDIO
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val cameraGranted = permissions[CAMERA] ?: false
            val recordAudioGranted = permissions[RECORD_AUDIO] ?: false

            if (!cameraGranted || !recordAudioGranted) {
                showPermissionExplanation()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startAdMob()
        requestPermissions()
    }

    private fun startAdMob() {
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@MainActivity) {}
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(arrayOf(CAMERA, RECORD_AUDIO))
    }

    private fun showPermissionExplanation() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_dialog_title))
            .setMessage(getString(R.string.permission_dialog_body))
            .setPositiveButton(getString(R.string.permission_dialog_ok)) { _, _ ->
                openAppSettings()
            }
            .setNegativeButton(getString(R.string.permission_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.parse("package:${packageName}")
        }
        startActivity(intent)
    }
}
