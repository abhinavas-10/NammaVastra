package com.example.sareevastra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    val db = FirebaseFirestore.getInstance()

    // Luxury Palette
    val gold = Color(0xFFFFD700)
    val darkPurple = Color(0xFF1B0B2E)
    val deepBlack = Color(0xFF0F0F0F)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(darkPurple, deepBlack)))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("LIST NEW SAREE",
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Luxury Image Preview Placeholder
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.05f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = gold, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("UPLOAD PREVIEW", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text("Product Details", color = gold, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(16.dp))

                // Luxury Inputs
                ModernLuxuryInput(value = name, onValueChange = { name = it }, label = "Saree Name")
                ModernLuxuryInput(value = description, onValueChange = { description = it }, label = "Description")
                ModernLuxuryInput(value = imageUrl, onValueChange = { imageUrl = it }, label = "Image URL Link")

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        ModernLuxuryInput(value = price, onValueChange = { price = it }, label = "Price (₹)", keyboardType = KeyboardType.Number)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ModernLuxuryInput(value = rating, onValueChange = { rating = it }, label = "Rating", keyboardType = KeyboardType.Decimal)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Premium Upload Button
                Button(
                    onClick = {
                        if (name.isNotEmpty() && price.isNotEmpty()) {
                            isUploading = true
                            val product = hashMapOf(
                                "name" to name,
                                "description" to description,
                                "imageUrl" to imageUrl,
                                "price" to (price.toLongOrNull() ?: 0L),
                                "rating" to (rating.toDoubleOrNull() ?: 0.0)
                            )
                            db.collection("products").add(product).addOnSuccessListener {
                                navController.navigate("seller_dashboard") {
                                    popUpTo("add_product") { inclusive = true }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gold,
                        contentColor = Color.Black
                    ),
                    enabled = !isUploading
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                    } else {
                        Text("PUBLISH COLLECTION", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            } // End Column
        } // End Scaffold
    } // End Box
} // End AddProductScreen

@Composable
fun ModernLuxuryInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 12.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color(0xFFFFD700),
            unfocusedLabelColor = Color.White.copy(alpha = 0.4f),
            focusedBorderColor = Color(0xFFFFD700),
            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
            cursorColor = Color(0xFFFFD700)
        )
    )
}