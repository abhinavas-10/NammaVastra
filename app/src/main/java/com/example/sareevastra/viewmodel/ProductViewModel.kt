package com.example.sareevastra.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.sareevastra.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class ProductViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var products = mutableStateListOf<Product>()
    var cartItems = mutableStateListOf<Product>()
    var wishlistItems = mutableStateListOf<Product>()

    init { fetchProducts() }

    fun fetchProducts() {
        db.collection("products").addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { doc ->
                    val p = doc.toObject(Product::class.java)
                    // CRITICAL: Ensure the ID from Firestore matches the object ID
                    p?.copy(id = doc.id)
                }
                products.clear()
                products.addAll(list)
            }
        }
    }

    fun addToCart(product: Product) = cartItems.add(product)
    fun removeFromCart(product: Product) = cartItems.remove(product)
    fun getTotalPrice(): Long = cartItems.sumOf { it.price }
    fun toggleWishlist(product: Product) {
        if (wishlistItems.contains(product)) wishlistItems.remove(product)
        else wishlistItems.add(product)
    }
    fun isWishlisted(product: Product): Boolean = wishlistItems.contains(product)
}