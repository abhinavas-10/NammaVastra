package com.example.sareevastra.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SellerLoginScreen(navController: NavController) {
    var isSignUp by remember { mutableStateOf(false) }
    var businessName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // New Blue & Gold Theme
    val darkBlue = Color(0xFF0D1B2A)
    val royalBlue = Color(0xFF1B263B)
    val goldColor = Color(0xFFFFD700)
    val bgGradient = Brush.verticalGradient(listOf(darkBlue, royalBlue, Color.Black))

    Box(modifier = Modifier.fillMaxSize().background(bgGradient)) {
        // Decorative background glow
        Box(
            modifier = Modifier
                .offset(x = (-80).dp, y = (-50).dp)
                .size(200.dp)
                .background(Color(0xFF415A77).copy(alpha = 0.2f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- BACK BUTTON ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isSignUp) "Partner With Us" else "Seller Portal",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = if (isSignUp) "Register your boutique" else "Sign in to manage your shop",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            if (isSignUp) {
                CustomLoginField(
                    value = businessName,
                    onValueChange = { businessName = it },
                    label = "Business Name",
                    icon = Icons.Default.Home
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            CustomLoginField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomLoginField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                icon = Icons.Default.Lock,
                isPassword = true,
                keyboardType = KeyboardType.Password
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ACTION BUTTON
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true
                        if (isSignUp) {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnSuccessListener { result ->
                                    val uid = result.user?.uid
                                    val profile = hashMapOf("uid" to uid, "businessName" to businessName, "email" to email, "role" to "seller")
                                    uid?.let {
                                        db.collection("users").document(it).set(profile)
                                            .addOnSuccessListener {
                                                isLoading = false
                                                navController.navigate("seller_dashboard")
                                            }
                                    }
                                }
                                .addOnFailureListener { e -> isLoading = false; Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() }
                        } else {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener {
                                    isLoading = false
                                    navController.navigate("seller_dashboard")
                                }
                                .addOnFailureListener { e -> isLoading = false; Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = goldColor, contentColor = darkBlue),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = darkBlue, modifier = Modifier.size(24.dp))
                else Text(if (isSignUp) "CREATE ACCOUNT" else "SIGN IN", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.padding(bottom = 30.dp)) {
                Text(text = if (isSignUp) "Already a seller? " else "Want to sell sarees? ", color = Color.White.copy(alpha = 0.6f))
                Text(
                    text = if (isSignUp) "Login" else "Register Now",
                    color = goldColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { isSignUp = !isSignUp }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomLoginField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White.copy(alpha = 0.5f)) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = Color(0xFFFFD700)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFFD700),
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}