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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
fun LoginScreen(navController: NavController) {
    var isSignUp by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // Purple & Gold Theme
    val primaryPurple = Color(0xFF6A1B9A)
    val darkPurple = Color(0xFF2D0B5A)
    val goldAccent = Color(0xFFFFD700)
    val bgGradient = Brush.verticalGradient(listOf(darkPurple, Color.Black))

    Box(modifier = Modifier.fillMaxSize().background(bgGradient)) {
        Box(
            modifier = Modifier
                .offset(x = 120.dp, y = (-60).dp)
                .size(300.dp)
                .background(primaryPurple.copy(alpha = 0.15f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back Button
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (isSignUp) "Join us,\nBeautiful" else "Welcome\nBack",
                color = Color.White,
                fontSize = 40.sp,
                lineHeight = 45.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                color = Color.White.copy(alpha = 0.08f)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    if (isSignUp) {
                        AuthInputField(value = name, onValueChange = { name = it }, label = "Full Name", icon = Icons.Default.Person, accentColor = goldAccent)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    AuthInputField(value = email, onValueChange = { email = it }, label = "Email", icon = Icons.Default.Email, keyboardType = KeyboardType.Email, accentColor = goldAccent)
                    Spacer(modifier = Modifier.height(12.dp))

                    AuthInputField(value = password, onValueChange = { password = it }, label = "Password", icon = Icons.Default.Lock, isPassword = true, keyboardType = KeyboardType.Password, accentColor = goldAccent)

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                isLoading = true
                                if (isSignUp) {
                                    auth.createUserWithEmailAndPassword(email, password)
                                        .addOnSuccessListener { result ->
                                            val uid = result.user?.uid
                                            val profile = hashMapOf("uid" to uid, "name" to name, "email" to email, "role" to "customer")
                                            uid?.let {
                                                db.collection("users").document(it).set(profile)
                                                    .addOnSuccessListener {
                                                        isLoading = false
                                                        navController.navigate("home")
                                                    }
                                            }
                                        }
                                        .addOnFailureListener { e -> isLoading = false; Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() }
                                } else {
                                    auth.signInWithEmailAndPassword(email, password)
                                        .addOnSuccessListener {
                                            isLoading = false
                                            navController.navigate("home")
                                        }
                                        .addOnFailureListener { e -> isLoading = false; Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = goldAccent, contentColor = darkPurple),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = darkPurple, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Text(if (isSignUp) "CREATE ACCOUNT" else "LOG IN", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.padding(bottom = 40.dp)) {
                Text(text = if (isSignUp) "Already a member? " else "New here? ", color = Color.White.copy(alpha = 0.6f))
                Text(
                    text = if (isSignUp) "Sign In" else "Register",
                    color = goldAccent,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { isSignUp = !isSignUp }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    accentColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White.copy(alpha = 0.6f)) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = accentColor) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentColor,
            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}