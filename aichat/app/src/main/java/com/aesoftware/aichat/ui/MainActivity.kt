package com.aesoftware.aichat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aesoftware.aichat.R
import com.aesoftware.aichat.utils.Constants
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Purchases.debugLogsEnabled = true
        Purchases.configure(PurchasesConfiguration.Builder(this, Constants.GOOGLE_API_KEY).build())
    }
}