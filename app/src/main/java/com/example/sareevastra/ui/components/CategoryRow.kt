package com.example.sareevastra.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoryRow() {

    val categories = listOf(

        "All",

        "Traditional",

        "Wedding",

        "Party Wear",

        "Silk",

        "Cotton"
    )

    var selectedCategory by remember {

        mutableStateOf("All")
    }

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(
                rememberScrollState()
            )
            .padding(horizontal = 12.dp)
    ) {

        categories.forEach { category ->

            AssistChip(

                onClick = {

                    selectedCategory = category
                },

                label = {

                    Text(category)
                },

                colors =
                    AssistChipDefaults.assistChipColors(

                        containerColor =

                            if (selectedCategory == category)

                                Color(0xFF8B0000)

                            else

                                Color.LightGray,

                        labelColor =

                            if (selectedCategory == category)

                                Color.White

                            else

                                Color.Black
                    ),

                modifier = Modifier.padding(
                    end = 8.dp
                )
            )
        }
    }
}