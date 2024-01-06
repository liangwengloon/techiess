package com.example.techiess.ui.home


import android.content.ContentValues
import android.content.Intent
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

        // Fetch data from Firebase
        fetchProductsFromFirebase()

        productAdapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                // Handle item click, e.g., open ProductDetailActivity
                val intent = Intent(requireContext(), productDetail::class.java)
                intent.putExtra("title", product.title)
                intent.putExtra("price", product.price)
                intent.putExtra("image", product.imageResId)
                intent.putExtra("OS", product.OS)
                intent.putExtra("battery", product.battery)
                intent.putExtra("camera", product.camera)
                intent.putExtra("category", product.category)
                intent.putExtra("desc", product.desc)
                intent.putExtra("display", product.display)
                intent.putExtra("ram", product.ram)
                intent.putExtra("rom", product.rom)
                intent.putExtra("productID", product.productID)

                Log.d(ContentValues.TAG, "DocumentSnapshot clicked with ID: ${product.productID}")

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
    private fun fetchProductsFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        val productList = mutableListOf<Product>()

        db.collection("products")
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


                    val product = Product(title, price, image, category, OS, battery, camera, desc, display, ram, rom, productID, documentID)
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
