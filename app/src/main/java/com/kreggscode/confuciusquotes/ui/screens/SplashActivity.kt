package com.kreggscode.confuciusquotes.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.kreggscode.confuciusquotes.MainActivity
import com.kreggscode.confuciusquotes.ui.theme.PremiumColors
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display (modern API for Android 15+)
        enableEdgeToEdge(
            statusBarStyle = androidx.activity.SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = androidx.activity.SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        
        // Configure window for edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Handle display cutout for Android 9+ (replaces deprecated LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = 
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    // Android 11+ (API 30+): Use ALWAYS for full edge-to-edge
                    android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
                } else {
                    // Android 9-10 (API 28-29): Use SHORT_EDGES (not deprecated on these versions)
                    android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
        }
        
        // Set status bar and navigation bar icons to white for dark background
        WindowCompat.getInsetsController(window, window.decorView)?.apply {
            isAppearanceLightStatusBars = false  // false = white icons
            isAppearanceLightNavigationBars = false  // false = white icons
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
        
        setContent {
            var shouldNavigate by remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                delay(3500) // Show splash for 3.5 seconds
                shouldNavigate = true
            }
            
            if (shouldNavigate) {
                LaunchedEffect(Unit) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                PremiumColors.DeepSpace,
                                PremiumColors.MidnightBlue,
                                PremiumColors.DeepSpace
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnimatedSplashScreen(
                    onSplashComplete = {
                        // This is handled by the LaunchedEffect above
                    }
                )
            }
        }
    }
}
