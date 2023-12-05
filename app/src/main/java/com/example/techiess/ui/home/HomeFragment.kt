package com.example.techiess.ui.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techiess.R
import com.example.techiess.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Initialize the adapter
        productAdapter = ProductAdapter(emptyList())
        recyclerView.adapter = productAdapter

        // Fetch data from Firebase
        fetchProductsFromFirebase()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    // Function to fetch products from Firebase
    private fun fetchProductsFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        val productList = mutableListOf<Product>()

        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val title = document.getString("title") ?: ""
                    val price = document.getString("price") ?: ""
                    val image = document.getString("image") ?: ""
                    val category = document.getString("category") ?: ""

                    val product = Product(title, price, image, category)
                    productList.add(product)
                }

                // Update the RecyclerView adapter with the fetched data
                productAdapter.setData(productList)
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }

        Log.d("HomeFragment", "Fetched ${productList.size} products from Firestore")
    }
}
