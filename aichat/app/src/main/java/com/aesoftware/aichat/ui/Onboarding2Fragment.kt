package com.aesoftware.aichat.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aesoftware.aichat.utils.Constants
import com.aesoftware.aichat.R
import com.aesoftware.aichat.databinding.FragmentOnboarding2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Onboarding2Fragment : Fragment() {
    private lateinit var binding: FragmentOnboarding2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboarding2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("chatGptInAppOnBoardingCheck", Context.MODE_PRIVATE)

        binding.nextBtn.setOnClickListener {
            sharedPreferences.apply {
                edit().putBoolean("onBoardingPassed", true).apply()
            }
                if (Constants.isStillPremium) {
                    findNavController().navigate(R.id.action_onboarding2Fragment_to_chatFragment)
                } else {
                    findNavController().navigate(R.id.action_onboarding2Fragment_to_inAppFragment)
                }
        }
    }
}