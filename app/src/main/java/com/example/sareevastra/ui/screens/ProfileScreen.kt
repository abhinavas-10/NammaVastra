package com.example.sareevastra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    // Luxury Theme Palette
    val goldAccent = Color(0xFFFFD700)
    val deepBlack = Color(0xFF0F0F0F)
    val darkPurple = Color(0xFF1B0B2E)
    val surfacePurple = Color(0xFF2D0B5A).copy(alpha = 0.3f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(darkPurple, deepBlack)))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("MY ACCOUNT", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 2.sp) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Avatar
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(Brush.linearGradient(listOf(goldAccent, Color.White)), CircleShape)
                        .padding(3.dp)
                        .background(deepBlack, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(60.dp), tint = goldAccent)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Abhinav", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = user?.email ?: "user@gmail.com", fontSize = 14.sp, color = Color.White.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(40.dp))

                // Action Menu - Using Standard Icons
                Surface(
                    color = surfacePurple,
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        // Changed ShoppingBag -> ShoppingCart
                        ProfileOptionItem(Icons.Default.ShoppingCart, "My Orders") { }
                        ProfileOptionItem(Icons.Default.Favorite, "Wishlist") { navController.navigate("wishlist") }
                        ProfileOptionItem(Icons.Default.Settings, "Settings") { }
                        ProfileOptionItem(Icons.Default.Info, "Help Center") { }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Logout Button - Using Standard ExitToApp Icon
                Button(
                    onClick = {
                        auth.signOut()
                        navController.navigate("login") { popUpTo("home") { inclusive = true } }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(bottom = 20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    // Changed Logout -> ExitToApp
                    Icon(Icons.Default.ExitToApp, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("SIGN OUT", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
        }
    }
}

@Composable
fun ProfileOptionItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.White.copy(alpha = 0.3f))
    }
}