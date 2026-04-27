package com.flash.letterly.presentation.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.flash.letterly.R
import com.flash.letterly.databinding.FragmentStartBinding
import com.flash.letterly.presentation.shared.GameMode
import dev.androidbroadcast.vbpd.viewBinding

class StartFragment : Fragment(R.layout.fragment_start) {
    private val binding: FragmentStartBinding by viewBinding(FragmentStartBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            classicBtn.setOnClickListener {
                findNavController().navigate(StartFragmentDirections.actionStartToGaming(GameMode.CLASSIC))
            }

            advancedBtn.setOnClickListener {
                findNavController().navigate(StartFragmentDirections.actionStartToGaming(GameMode.ADVANCED))
            }

            expertBtn.setOnClickListener {
                findNavController().navigate(StartFragmentDirections.actionStartToGaming(GameMode.EXPERT))
            }
        }
    }
}
