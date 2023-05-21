package com.aesoftware.aichat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aesoftware.aichat.databinding.FragmentSettingsBinding
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.getCustomerInfoWith
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            Purchases.sharedInstance.getCustomerInfoWith({ error -> /* Optional error handling */ }) { purchaserInfo ->
                if (purchaserInfo.entitlements["pro"]?.isActive == true) {
                    premiumConstraint.visibility = View.GONE
                } else {
                    premiumConstraint.visibility = View.VISIBLE
                    getPremium.setOnClickListener {
                        findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToInAppFragment())
                    }
                }
            }
            backBtn.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}