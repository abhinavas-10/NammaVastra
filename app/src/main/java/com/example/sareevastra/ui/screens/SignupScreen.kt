package com.example.sareevastra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sareevastra.auth.FirebaseAuthManager

@Composable
fun SignupScreen(
    navController: NavController
) {

    var email by remember {

        mutableStateOf("")
    }

    var password by remember {

        mutableStateOf("")
    }

    var message by remember {

        mutableStateOf("")
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(

            text = "Create Account",

            style =
                MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(

            value = email,

            onValueChange = {

                email = it
            },

            label = {

                Text("Email")
            },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(

            value = password,

            onValueChange = {

                password = it
            },

            label = {

                Text("Password")
            },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(

            onClick = {

                FirebaseAuthManager.signup(

                    email = email,

                    password = password,

                    onSuccess = {

                        navController.navigate("home")
                    },

                    onError = {

                        message = it
                    }
                )
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Sign Up")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(message)
    }
}