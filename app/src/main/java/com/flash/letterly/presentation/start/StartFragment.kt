package com.flash.letterly.presentation.start

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.flash.letterly.R
import com.flash.letterly.databinding.FragmentStartBinding
import com.flash.letterly.presentation.shared.GameMode
import dev.androidbroadcast.vbpd.viewBinding

class StartFragment : Fragment(R.layout.fragment_start) {
    private val binding: FragmentStartBinding by viewBinding(FragmentStartBinding::bind)
    private val viewModel: StartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            fiveBySixBtn.setOnClickListener {
                findNavController().navigate(StartFragmentDirections.actionStartToGaming(GameMode.CLASSIC))
            }

            sixBySevenBtn.setOnClickListener {
                findNavController().navigate(StartFragmentDirections.actionStartToGaming(GameMode.ADVANCED))
            }

            sevenByEightMode.setOnClickListener {
                findNavController().navigate(StartFragmentDirections.actionStartToGaming(GameMode.EXPERT))
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }
}