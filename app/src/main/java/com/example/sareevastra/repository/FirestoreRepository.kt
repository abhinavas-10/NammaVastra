package com.example.sareevastra.repository

import com.example.sareevastra.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    private val db =
        FirebaseFirestore.getInstance()

    fun getProducts(

        onResult: (List<Product>) -> Unit
    ) {

        db.collection("products")
            .get()

            .addOnSuccessListener { result ->

                val productList =
                    mutableListOf<Product>()

                for (document in result) {

                    val product =

                        document.toObject(
                            Product::class.java
                        ).copy(

                            id = document.id
                        )

                    productList.add(product)
                }

                onResult(productList)
            }
    }
}