package com.example.techiess.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.techiess.Checkout
import com.example.techiess.Orders
import com.example.techiess.R
import com.example.techiess.changePassword
import com.example.techiess.ui.settings.SettingsViewModel

class Settings : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var btnGoToOrders: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find your CardView by its ID
        val linearLayoutCard1 = view.findViewById<LinearLayout>(R.id.orders)

        // Set the click listener for Card 1
        linearLayoutCard1.setOnClickListener {
            onCard1Click()
        }

        val linearLayoutCard2 = view.findViewById<LinearLayout>(R.id.changePassword)

        // Set the click listener for Card 1
        linearLayoutCard2.setOnClickListener {
            onCard2Click()
        }

        val linearLayoutCard3 = view.findViewById<LinearLayout>(R.id.contactUs)

        // Set the click listener for Card 1
        linearLayoutCard3.setOnClickListener {
            onCard3Click()
        }

    }

    private fun onCard1Click() {
        // Handle click for Card 1
        val intent = Intent(requireContext(), Orders::class.java)
        startActivity(intent)
    }
    private fun onCard2Click() {
        // Handle click for Card 1
        val intent = Intent(requireContext(), changePassword::class.java)
        startActivity(intent)
    }
    private fun onCard3Click() {
        // Handle click for Card 1
        val intent = Intent(requireContext(), Orders::class.java)
        startActivity(intent)
    }
    private fun onCard4Click() {
        // Handle click for Card 1
        val intent = Intent(requireContext(), Orders::class.java)
        startActivity(intent)
    }
    private fun onCard5Click() {
        // Handle click for Card 1
        val intent = Intent(requireContext(), Orders::class.java)
        startActivity(intent)
    }
    private fun onCard6Click() {
        // Handle click for Card 1
        val intent = Intent(requireContext(), Orders::class.java)
        startActivity(intent)
    }
}
