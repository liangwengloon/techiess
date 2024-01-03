package com.example.techiess.ui.main

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.techiess.R
import com.example.techiess.databinding.FragmentComparisonBinding
import com.example.techiess.ui.home.Product

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private var _binding: FragmentComparisonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComparisonBinding.inflate(inflater, container, false)
        val root = binding.root

        // Retrieve product details from arguments
        val product = arguments?.getSerializable(ARG_PRODUCT) as? Product
        if (product != null) {
            // Update UI with product details
            updateProductDetails(product)
        }

        return root
    }

     fun updateProductDetails(product: Product) {

        val formattedPrice = String.format("%.2f", product.price)

        // Update TextViews
        binding.productTitleDetail.text = product.title
        binding.productPriceDetail.text = getString(R.string.price_format, formattedPrice)
        binding.osDetail.text = product.OS
        binding.batteryDetail.text = product.battery
        binding.cameraDetail.text = product.camera
        binding.categoryDetail.text = product.category
        binding.displayDetail.text = product.display
        binding.ramDetail.text = product.ram
        binding.romDetail.text = product.rom
        binding.descDetail.text = product.desc

        // Update ImageView using Glide for efficient image loading
        Glide.with(this)
            .load(product.imageResId)
            .into(binding.productImageDetail)
         Log.d(ContentValues.TAG, "Selected Image : ${product.imageResId}")

     }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PRODUCT = "product"

        @JvmStatic
        fun newInstance(product: Product): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PRODUCT, product)
                }
            }
        }
    }

}
