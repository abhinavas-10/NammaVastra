package com.example.sareevastra.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.sareevastra.ui.screens.*
import com.example.sareevastra.viewmodel.ProductViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: ProductViewModel = viewModel()

    // We start at role_selection to ensure you can see your login options
    NavHost(navController = navController, startDestination = "role_selection") {

        // --- AUTH & SELECTION ---
        composable("splash") { SplashScreen(navController) }
        composable("role_selection") { RoleSelectionScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("seller_login") { SellerLoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }

        // --- USER FLOW ---
        composable("home") { HomeScreen(navController, viewModel) }

        composable("product_detail/{productId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                navController = navController,
                viewModel = viewModel,
                productId = id
            )
        }

        composable("cart") { CartScreen(navController, viewModel) }
        composable("wishlist") { WishlistScreen(navController, viewModel) }
        composable("profile") { ProfileScreen(navController) }
        composable("checkout") { CheckoutScreen(navController, viewModel) }
        composable("order_success") { OrderSuccessScreen(navController) }

        // --- SELLER FLOW ---
        composable("seller_dashboard") { SellerDashboardScreen(navController) }
        composable("add_product") { AddProductScreen(navController) }
    }
}