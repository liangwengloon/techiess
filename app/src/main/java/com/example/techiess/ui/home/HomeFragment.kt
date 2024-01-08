package com.example.techiess.ui.home


import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techiess.R
import com.example.techiess.databinding.FragmentHomeBinding
import com.example.techiess.productDetail
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

        val spinner = binding.spinnerPriceRange
        val priceRanges = arrayOf("All", "RM1-1000", "RM1001-3000", "RM3001-5000", "RM5001-10000")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, priceRanges)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        // Set up a listener for item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Handle item selection, fetch products based on the selected price range
                when (position) {
                    0 -> fetchProductsFromFirebase(0.0, Double.MAX_VALUE) // All
                    1 -> fetchProductsFromFirebase(1.0, 1000.0) // RM1-1000
                    2 -> fetchProductsFromFirebase(1001.0, 3000.0) // RM1001-3000
                    3 -> fetchProductsFromFirebase(3001.0, 5000.0) // RM3001-5000
                    4 -> fetchProductsFromFirebase(5001.0, 10000.0)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                fetchProductsFromFirebase(0.0, Double.MAX_VALUE)
            }
        }
        // Fetch data from Firebase


        productAdapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                // Handle item click, e.g., open ProductDetailActivity
                val intent = Intent(requireContext(), productDetail::class.java)
                intent.putExtra("title", product.title)
                intent.putExtra("price", product.price)
                intent.putExtra("image", product.imageResId)
                intent.putExtra("category", product.category)

                intent.putExtra("OS", product.OS)
                intent.putExtra("battery", product.battery)
                intent.putExtra("camera", product.camera)

                intent.putExtra("desc", product.desc)
                intent.putExtra("display", product.display)
                intent.putExtra("ram", product.ram)
                intent.putExtra("rom", product.rom)

                intent.putExtra("productID", product.productID)
                intent.putExtra("documentID", product.documentID)

                startActivity(intent)
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    // Function to fetch products from Firebase
    // Function to fetch products from Firebase with price filtering
    private fun fetchProductsFromFirebase(minPrice: Double, maxPrice: Double) {
        val db = FirebaseFirestore.getInstance()
        val productList = mutableListOf<Product>()

        db.collection("products")
            .whereGreaterThanOrEqualTo("price", minPrice)
            .whereLessThanOrEqualTo("price", maxPrice)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val title = document.getString("title") ?: ""
                    val price = document.getDouble("price") ?: 0.0
                    val image = document.getString("image") ?: ""
                    val category = document.getString("category") ?: ""
                    val OS = document.getString("OS") ?: ""
                    val battery = document.getString("battery") ?: ""
                    val camera = document.getString("camera") ?: ""
                    val desc = document.getString("desc") ?: ""
                    val display = document.getString("display") ?: ""
                    val ram = document.getString("ram") ?: ""
                    val rom = document.getString("rom") ?: ""
                    val productID = document.id
                    var documentID = document.id

                    val product = Product(
                        title, price, image, category, OS, battery, camera, desc, display, ram, rom,
                        productID, documentID
                    )
                    productList.add(product)
                }

                // Update the RecyclerView adapter with the filtered data
                productAdapter.setData(productList)
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }

        Log.d("HomeFragment", "Fetched ${productList.size} products from Firestore")
    }


}
