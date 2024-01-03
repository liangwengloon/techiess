package com.example.techiess

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.techiess.ui.main.SectionsPagerAdapter
import com.example.techiess.databinding.ActivityComparisonBinding
import com.example.techiess.ui.home.Product
import com.example.techiess.ui.main.PlaceholderFragment
import com.google.firebase.firestore.FirebaseFirestore

class Comparison : AppCompatActivity() {

    private lateinit var binding: ActivityComparisonBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var product1: Product
    private lateinit var product2: Product
    private val fragments: MutableList<PlaceholderFragment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityComparisonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager: ViewPager = binding.viewPager
        val tabs: TabLayout = binding.tabs

        // Initialize product1 and product2
        product1 = Product() // Replace Product() with the actual initialization logic
        product2 = Product() // Replace Product() with the actual initialization logic

        // Create fragments with initialized products
        val fragment1 = PlaceholderFragment.newInstance(product1)
        val fragment2 = PlaceholderFragment.newInstance(product2)

        // Initialize SectionsPagerAdapter with fragments
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, fragment1, fragment2)

        // Set up ViewPager and TabLayout
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)

        // Retrieve product details from intent
        val product1DocId = intent.getStringExtra("PRODUCT_1")
        Log.d(ContentValues.TAG, "Selected Product 1 Document ID: ${product1DocId}")
        val product2DocId = intent.getStringExtra("PRODUCT_2")
        Log.d(ContentValues.TAG, "Selected Product 2 Document ID: ${product2DocId}")

        // Fetch product details from Firestore
        if (product1DocId != null) {
            fetchProductDetails(product1DocId) { product ->
                product1 = product
                updateFragmentData(0)
            }
        }

        if (product2DocId != null) {
            fetchProductDetails(product2DocId) { product ->
                product2 = product
                updateFragmentData(1)
            }
        }

        val backButton = findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun fetchProductDetails(docId: String, callback: (Product) -> Unit) {
        // Log statement to trace the flow
        Log.d("Comparison", "fetchProductDetails: Start")

        // Assuming you have a reference to your Firestore database
        val db = FirebaseFirestore.getInstance()

        // Assuming "products" is the collection in Firestore
        val productRef = db.collection("products").document(docId)

        productRef.get()
            .addOnSuccessListener { document ->
                // Log statement to trace the flow
                Log.d("Comparison", "fetchProductDetails: OnSuccessListener")

                if (document.exists()) {
                    // Convert Firestore document to Product object
                    val product = document.toObject(Product::class.java)
                    if (product != null) {
                        // Invoke the callback with the retrieved product
                        callback(product)
                    } else {
                        // Handle the case where conversion to Product is unsuccessful
                        Log.e("Comparison", "fetchProductDetails: Product conversion failed")
                    }
                } else {
                    // Handle the case where the document doesn't exist
                    Log.e("Comparison", "fetchProductDetails: Document doesn't exist")
                }
            }
            .addOnFailureListener { exception ->
                // Handle failures in fetching data from Firestore
                Log.e("Comparison", "fetchProductDetails: Failure - $exception")
            }

        // Log statement to trace the flow
        Log.d("Comparison", "fetchProductDetails: End")
    }


    private fun updateFragmentData(position: Int) {
        val fragment = sectionsPagerAdapter.getItem(position) as PlaceholderFragment
        fragments.add(position, fragment)

        when (position) {
            0 -> fragment.updateProductDetails(product1)
            1 -> fragment.updateProductDetails(product2)
        }
    }
}

