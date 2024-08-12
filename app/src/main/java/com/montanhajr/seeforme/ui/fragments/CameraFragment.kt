package com.montanhajr.seeforme.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.montanhajr.seeforme.R
import com.montanhajr.seeforme.ui.screens.SeeForMeScreen

class CameraFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            val prompt = arguments?.getString("prompt").takeIf { !it.isNullOrEmpty() }

            setContent {
                SeeForMeScreen(prompt ?: stringResource(id = R.string.seeForMe_prompt))
            }
        }
    }
}
