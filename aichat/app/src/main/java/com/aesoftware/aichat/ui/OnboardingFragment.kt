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
import com.aesoftware.aichat.databinding.FragmentOnboardingBinding
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.getCustomerInfoWith
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("chatGptInAppOnBoardingCheck", Context.MODE_PRIVATE)

        sharedPreferences.apply {
            val onBoardingPassed = getBoolean("onBoardingPassed",false)

            if (onBoardingPassed) {
                Purchases.sharedInstance.getCustomerInfoWith({ error -> /* Optional error handling */ }) { purchaserInfo ->
                    if (purchaserInfo.entitlements["pro"]?.isActive == true) {
                        // Grant user "pro" access
                        Constants.isStillPremium = true
                        findNavController().navigate(R.id.action_onboardingFragment_to_chatFragment)
                    }else {
                        Constants.isStillPremium = false
                        findNavController().navigate(R.id.action_onboardingFragment_to_inAppFragment)
                    }
                }
            }
        }

        binding.nextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_onboarding2Fragment)
        }
    }
}