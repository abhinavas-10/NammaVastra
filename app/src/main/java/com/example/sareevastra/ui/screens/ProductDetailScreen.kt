package com.example.sareevastra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.sareevastra.viewmodel.ProductViewModel

@Composable
fun ProductDetailScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    productId: String
) {
    // Lookup product by ID
    val product = viewModel.products.find { it.id == productId }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Product Not Found. ID: $productId", color = Color.White)
        }
    } else {
        // Replace this with your actual luxury detail UI code
        Column(modifier = Modifier.fillMaxSize()) {
            Text("Showing: ${product.name}", color = Color.White)
            // ... rest of your UI
        }
    }
}