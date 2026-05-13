package com.example.sareevastra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sareevastra.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController, viewModel: ProductViewModel) {
    // Structured Address States
    var houseName by remember { mutableStateOf("") }
    var areaDetails by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }

    val gold = Color(0xFFFFD700)
    val darkPurple = Color(0xFF1B0B2E)
    val deepBlack = Color(0xFF0F0F0F)

    // Form Validation: Ensure all fields are filled
    val isFormValid = houseName.isNotBlank() && areaDetails.isNotBlank() &&
            city.isNotBlank() && pincode.length >= 6

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(darkPurple, deepBlack)))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("CHECKOUT", fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 2.sp) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
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
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Shipping Address",
                    color = gold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Input Fields with Standard Icons
                CheckoutInput(houseName, "House Name / No.", Icons.Default.Home) { houseName = it }
                Spacer(modifier = Modifier.height(12.dp))

                CheckoutInput(areaDetails, "Area / Street / Colony", Icons.Default.LocationOn) { areaDetails = it }
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        // Using Place instead of PinDrop to avoid errors
                        CheckoutInput(pincode, "Pincode", Icons.Default.Place) { pincode = it }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        CheckoutInput(city, "City", Icons.Default.LocationOn) { city = it }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Order Total Display
                Surface(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Grand Total", color = Color.Gray, fontSize = 14.sp)
                        Text(
                            "₹${viewModel.getTotalPrice()}",
                            color = gold,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        viewModel.cartItems.clear()
                        navController.navigate("order_success")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gold,
                        disabledContainerColor = Color.White.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = isFormValid
                ) {
                    Text(
                        "PLACE ORDER",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun CheckoutInput(
    value: String,
    label: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(icon, null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp)) },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color(0xFFFFD700),
            unfocusedLabelColor = Color.Gray,
            focusedBorderColor = Color(0xFFFFD700),
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
        )
    )
}