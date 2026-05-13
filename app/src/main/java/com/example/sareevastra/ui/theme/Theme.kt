package com.example.sareevastra.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(

    primary = Color(0xFFB00020),

    secondary = Color(0xFFCF6679),

    background = Color(0xFF121212),

    surface = Color(0xFF1E1E1E)
)

private val LightColorScheme = lightColorScheme(

    primary = Color(0xFF8B0000),

    secondary = Color(0xFFB22222),

    background = Color(0xFFF8F5F2),

    surface = Color(0xFFFFFFFF)
)

@Composable
fun SareeVastraTheme(

    darkTheme: Boolean =
        isSystemInDarkTheme(),

    dynamicColor: Boolean = false,

    content: @Composable () -> Unit
) {

    val colorScheme = when {

        dynamicColor &&
                Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.S -> {

            val context =
                LocalContext.current

            if (darkTheme)
                dynamicDarkColorScheme(context)

            else
                dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme

        else -> LightColorScheme
    }

    MaterialTheme(

        colorScheme = colorScheme,

        content = content
    )
}