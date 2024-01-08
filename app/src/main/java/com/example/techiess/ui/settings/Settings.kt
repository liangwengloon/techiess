package com.example.techiess.ui.settings

import android.app.AlertDialog
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
import com.example.techiess.MainActivity
import com.example.techiess.Orders
import com.example.techiess.R
import com.example.techiess.aboutUs
import com.example.techiess.changePassword
import com.example.techiess.contactUs
import com.example.techiess.myAddress
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

        linearLayoutCard2.setOnClickListener {
            onCard2Click()
        }

        val linearLayoutCard3 = view.findViewById<LinearLayout>(R.id.contactUs)

        linearLayoutCard3.setOnClickListener {
            onCard3Click()
        }

        val linearLayoutCard4 = view.findViewById<LinearLayout>(R.id.aboutUs)

        linearLayoutCard4.setOnClickListener {
            onCard4Click()
        }

        val linearLayoutCard5 = view.findViewById<LinearLayout>(R.id.myAddress)

        linearLayoutCard5.setOnClickListener {
            onCard5Click()

        }

        val linearLayoutCard6 = view.findViewById<LinearLayout>(R.id.settings2)

        linearLayoutCard6.setOnClickListener {
            onCard6Click()

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
        val intent = Intent(requireContext(), contactUs::class.java)
        startActivity(intent)
    }
    private fun onCard4Click() {
        // Handle click for Card 1
        val intent = Intent(requireContext(), aboutUs::class.java)
        startActivity(intent)
    }
    private fun onCard5Click() {
        // Handle click for Card 1
        val intent = Intent(requireContext(), myAddress::class.java)
        startActivity(intent)
    }
    private fun onCard6Click() {
        // Handle click for Card 1
        AlertDialog.Builder(requireContext())
            .setTitle("Log Out")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { dialog, which ->
                // User clicked Yes, proceed to log out
                logOutAndRedirect()
            }
            .setNegativeButton("No") { dialog, which ->
                // User clicked No, dismiss the dialog
                dialog.dismiss()
            }
            .show()
    }

    private fun logOutAndRedirect() {
        // Handle the log out logic here

        val intent = Intent(requireContext(), MainActivity::class.java)

        startActivity(intent)
        requireActivity().finish()
    }
}
