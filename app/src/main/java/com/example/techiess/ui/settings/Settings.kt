package com.example.techiess.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.techiess.Checkout
import com.example.techiess.Orders
import com.example.techiess.R
import com.example.techiess.ui.settings.SettingsViewModel

class Settings : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var btnGoToOrders: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize the button
        btnGoToOrders = view.findViewById(R.id.goToOrder) // Replace with your actual button ID

        btnGoToOrders.setOnClickListener {
            // Navigate to the checkout activity
            val intent = Intent(requireContext(), Orders::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Any additional setup after view creation
    }
}
