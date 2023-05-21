package com.aesoftware.aichat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aesoftware.aichat.R
import androidx.navigation.fragment.findNavController
import com.aesoftware.aichat.databinding.FragmentInAppBinding
import com.aesoftware.aichat.utils.Constants
import com.revenuecat.purchases.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InAppFragment() : Fragment() {

    private lateinit var binding: FragmentInAppBinding

    private var listInt = 0
    var pacList = arrayListOf<Package>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        clickPremiumItem()

        binding.closeIcon.setOnClickListener {
            findNavController().navigate(InAppFragmentDirections.actionInAppFragmentToChatFragment())
        }

        Purchases.sharedInstance.getOfferingsWith({
            // An error occurred
        }) { offerings ->
            offerings.current?.weekly.also {
                binding.weeklyTV2.text = it?.product?.price
            }

            offerings.current?.monthly.also {
                binding.monthlyTV2.text = it?.product?.price
            }

            offerings.current?.annual.also {
                binding.annualTV2.text = it?.product?.price
            }

            /*
            when (premiumPackage) {
                "\$rc_weekly" -> a = offerings.current?.getPackage(identifier = premiumPackage)
                "\$rc_monthly" -> a =
                    offerings.current?.getPackage(identifier = premiumPackage)
                "\$rc_annual" -> a =
                    offerings.current?.getPackage(identifier = premiumPackage)
            }
             */
            //a = offerings.current?.weekly.

            offerings.current?.availablePackages?.forEach {
                pacList.add(it)
            }


        }

        binding.tryBtn.setOnClickListener {
            Purchases.sharedInstance.purchasePackageWith(
                requireActivity(),
                pacList[listInt],
                onError = { error, userCancelled -> /* No purchase */ },
                onSuccess = { product, customerInfo ->
                    if (customerInfo.entitlements["pro"]?.isActive == true) {
                        Constants.isStillPremium = true
                        findNavController().navigate(R.id.action_inAppFragment_to_chatFragment)
                    }
                })
        }
    }

    private fun reset() {
        binding.apply {
            val viewList = listOf(weeklyPlanView, monthlyPlanView, annualPlanView)
            val iconList = listOf(iconWeekly, iconMonthly, iconAnnual)

            viewList.forEach { view ->
                view.setBackgroundResource(R.drawable.inapp_unclicked)
            }
            iconList.forEach { icon ->
                icon.setImageResource(R.drawable.icon_unselected)
            }
        }
    }

    private fun clickPremiumItem() {

        binding.apply {
            val viewList = listOf(weeklyPlanView, monthlyPlanView, annualPlanView)
            val iconList = listOf(iconWeekly, iconMonthly, iconAnnual)

            viewList.forEachIndexed { index, view ->
                view.setOnClickListener {

                    pacList.forEach {
                        println(it)
                    }
                    reset()
                    iconList.forEachIndexed { iconIndex, icon ->
                        if (index == iconIndex) {
                            when (iconIndex) {
                                0 -> {
                                    listInt = 2
                                }
                                1 -> {
                                    listInt = 0
                                }
                                2 -> {
                                    listInt = 1
                                }
                            }
                            icon.setImageResource(R.drawable.icon_selected)
                        } else {
                            icon.setImageResource(R.drawable.icon_unselected)
                        }
                    }
                    view.setBackgroundResource(R.drawable.inapp_clicked)
                }
            }
        }

    }
}