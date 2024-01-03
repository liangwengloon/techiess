package com.example.techiess.ui.main

import android.content.ContentValues
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.techiess.R
import com.example.techiess.databinding.FragmentComparisonBinding
import com.example.techiess.ui.home.Product
import com.squareup.picasso.Picasso

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

        // Update ImageView using Picasso for efficient image loading
        if (!product.image.isNullOrEmpty()) {
            // Update ImageView using Picasso for efficient image loading
            Picasso.get()
                .load(product.image)
                .into(binding.productImageDetail)
        } else {
            // Handle the case where imageResId is empty or null
            Log.e(ContentValues.TAG, "Invalid imageResId: ${product.image}")
            // You can provide a placeholder image or handle this case based on your requirements
            // For example, you might want to set a default image or show an error placeholder.
        }
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
