package com.montanhajr.seeforme

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest.permission.CAMERA
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchCamera()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue launching the camera.
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied.
            }
        }

    private fun launchCamera() {
        requestPermissionLauncher.launch(CAMERA)
    }
}