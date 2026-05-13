package com.example.sareevastra.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {

    var startAnimation by remember {

        mutableStateOf(false)
    }

    val scaleAnimation = animateFloatAsState(

        targetValue =
            if (startAnimation) 1f else 0.7f,

        animationSpec = tween(
            durationMillis = 1200
        ),

        label = ""
    )

    LaunchedEffect(Unit) {

        startAnimation = true

        delay(2500)

        navController.navigate("login") {

            popUpTo("splash") {

                inclusive = true
            }
        }
    }

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(

                brush = Brush.verticalGradient(

                    colors = listOf(

                        Color(0xFF4E0000),

                        Color(0xFF8B0000),

                        Color(0xFF1A1A1A)
                    )
                )
            ),

        contentAlignment = Alignment.Center
    ) {

        Column(

            horizontalAlignment =
                Alignment.CenterHorizontally,

            modifier = Modifier.scale(
                scaleAnimation.value
            )
        ) {

            Text(

                text = "Saree Vastra",

                fontSize = 42.sp,

                fontWeight = FontWeight.Bold,

                color = Color.White
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(

                text =
                    "Traditional Fashion Store",

                color =
                    Color.White.copy(alpha = 0.8f),

                style =
                    MaterialTheme.typography.bodyLarge
            )
        }
    }
}