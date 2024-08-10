package com.montanhajr.seeforme.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.montanhajr.seeforme.ui.screens.FindForMeScreen
import com.montanhajr.seeforme.ui.screens.ReadForMeScreen

class FindFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FindForMeScreen()
            }
        }
    }
}
