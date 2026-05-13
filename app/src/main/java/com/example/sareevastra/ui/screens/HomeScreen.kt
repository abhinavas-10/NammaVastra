package com.example.sareevastra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sareevastra.ui.components.*
import com.example.sareevastra.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: ProductViewModel) {

    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var priceRange by remember { mutableStateOf(0f..10000f) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val goldAccent = Color(0xFFFFD700)
    val darkPurple = Color(0xFF2D0B5A)
    val deepBlack = Color(0xFF0F0F0F)

    val filteredProducts = viewModel.products.filter { product ->
        val matchesSearch = product.name.contains(searchText, ignoreCase = true)
        val matchesCategory = if (selectedCategory == "All") true
        else product.description.contains(selectedCategory, ignoreCase = true)
        val matchesPrice = product.price.toFloat() in priceRange
        matchesSearch && matchesCategory && matchesPrice
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF1B0B2E)
        ) {
            FilterSheetContent(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                priceRange = priceRange,
                onPriceChange = { priceRange = it }
            )
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = deepBlack) {
                val navItems = listOf(
                    Triple("home", Icons.Default.Home, "Home"),
                    Triple("wishlist", Icons.Default.Favorite, "Wishlist"),
                    Triple("cart", Icons.Default.ShoppingCart, "Cart"),
                    Triple("profile", Icons.Default.Person, "Profile")
                )
                navItems.forEach { (route, icon, label) ->
                    NavigationBarItem(
                        selected = currentRoute == route,
                        onClick = { if (currentRoute != route) navController.navigate(route) },
                        icon = { Icon(icon, null) },
                        label = { Text(label, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = goldAccent, unselectedIconColor = Color.Gray)
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Brush.verticalGradient(listOf(darkPurple, deepBlack)))
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(end = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f)) {
                        HomeHeader(searchText, onSearchChange = { searchText = it })
                    }
                    IconButton(
                        onClick = { showFilterSheet = true },
                        modifier = Modifier.background(goldAccent, RoundedCornerShape(12.dp)).size(48.dp)
                    ) { Icon(Icons.Default.List, "Filter", tint = darkPurple) }
                }
                CategoryRow()
                BannerCarousel()
                SectionHeader("Exclusive Collection", goldAccent)
            }

            items(filteredProducts) { product ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    ProductCard(
                        product = product,
                        onClick = { navController.navigate("details/${product.id}") },
                        onAddToCart = { viewModel.addToCart(product) },
                        onWishlistClick = { viewModel.toggleWishlist(product) },
                        isWishlisted = viewModel.isWishlisted(product)
                    )
                }
            }
        }
    }
}

// --- MISSING COMPONENTS DEFINED HERE ---

@Composable
fun SectionHeader(title: String, themeColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("See All", color = themeColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FilterSheetContent(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    priceRange: ClosedFloatingPointRange<Float>,
    onPriceChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column(modifier = Modifier.padding(24.dp).navigationBarsPadding()) {
        Text("Refine Search", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Price Range", color = Color.White)
        RangeSlider(
            value = priceRange,
            onValueChange = onPriceChange,
            valueRange = 0f..10000f,
            colors = SliderDefaults.colors(thumbColor = Color(0xFFFFD700), activeTrackColor = Color(0xFFFFD700))
        )
        Spacer(modifier = Modifier.height(24.dp))
        listOf("All", "Silk", "Cotton", "Party Wear").forEach { category ->
            Row(modifier = Modifier.fillMaxWidth().clickable { onCategorySelected(category) }.padding(vertical = 12.dp)) {
                Text(category, color = if(selectedCategory == category) Color(0xFFFFD700) else Color.White)
            }
        }
    }
}