package com.example.techiess.ui.compare

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.techiess.Comparison
import com.example.techiess.R
import com.example.techiess.databinding.FragmentCompareBinding
import com.example.techiess.ui.home.Product
import com.example.techiess.ui.home.ProductAdapter
import com.google.firebase.firestore.FirebaseFirestore

class CompareFragment : Fragment() {

    private var _binding: FragmentCompareBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private lateinit var spinnerProduct1: Spinner
    private lateinit var spinnerProduct2: Spinner
    private lateinit var btnContinue: Button
    private lateinit var productList: List<Product>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompareBinding.inflate(inflater, container, false)
        val root: View = binding.root

        spinnerProduct1 = root.findViewById(R.id.spinnerProduct1)
        spinnerProduct2 = root.findViewById(R.id.spinnerProduct2)
        btnContinue = root.findViewById(R.id.btnContinue)

        // Initialize RecyclerView and ProductAdapter
        productAdapter = ProductAdapter(emptyList())

        // Fetch products from Firestore
        fetchProducts()

        btnContinue.setOnClickListener {
            // Check if productList is not null and contains items
            if (::productList.isInitialized && productList.isNotEmpty()) {
                // Get the selected product documents
                val selectedProduct1 = productList[spinnerProduct1.selectedItemPosition]
                val selectedProduct2 = productList[spinnerProduct2.selectedItemPosition]

                // Log the document IDs
                Log.d(ContentValues.TAG, "Selected Product 1 Document ID: ${selectedProduct1.documentID}")
                Log.d(ContentValues.TAG, "Selected Product 2 Document ID: ${selectedProduct2.documentID}")

                // Create an intent to start the ComparisonActivity
                val intent = Intent(requireContext(), Comparison::class.java).apply {
                    putExtra("PRODUCT_1", selectedProduct1.documentID)
                    putExtra("PRODUCT_2", selectedProduct2.documentID)
                    // Add other fields as needed
                }

                // Start the ComparisonActivity
                startActivity(intent)
            } else {
                // Handle the case where productList is not initialized or empty
                Log.e(ContentValues.TAG, "Product list is not initialized or empty.")
            }
        }

        return root
    }

    private fun fetchProducts() {
        val db = FirebaseFirestore.getInstance()

        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                productList = result.map { documentSnapshot ->
                    val product = documentSnapshot.toObject(Product::class.java)
                    // Update the Product class to include a documentID property
                    product?.documentID = documentSnapshot.id
                    product
                }.filterNotNull()

                // Update the adapter with the fetched data
                productAdapter.setData(productList)

                // Populate spinners with product titles
                val productTitles = productList.map { it.title }.toTypedArray()
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, productTitles)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinnerProduct1.adapter = adapter
                spinnerProduct2.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting products", exception)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
