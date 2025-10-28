package com.kreggscode.confuciusquotes

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kreggscode.confuciusquotes.navigation.NavGraph
import com.kreggscode.confuciusquotes.navigation.Screen
import com.kreggscode.confuciusquotes.notifications.NotificationScheduler
import com.kreggscode.confuciusquotes.ui.components.BottomNavItem
import com.kreggscode.confuciusquotes.ui.components.PremiumFloatingBottomBar
import com.kreggscode.confuciusquotes.ui.screens.AnimatedSplashScreen
import com.kreggscode.confuciusquotes.ui.theme.AnimatedGradientBackground
import com.kreggscode.confuciusquotes.ui.theme.ConfuciusTheme
import com.kreggscode.confuciusquotes.ui.theme.PremiumColors
import com.kreggscode.confuciusquotes.viewmodel.ChatViewModel
import com.kreggscode.confuciusquotes.viewmodel.QuoteViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    private val quoteViewModel: QuoteViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display with proper insets handling for Android 15+
        // This is the recommended approach for backward compatibility
        enableEdgeToEdge(
            statusBarStyle = androidx.activity.SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = androidx.activity.SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        
        // Configure window for edge-to-edge - works for all Android versions
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
        
        // Set status bar and navigation bar icons to WHITE (light icons on dark background)
        WindowCompat.getInsetsController(window, window.decorView)?.apply {
            isAppearanceLightStatusBars = false  // false = white icons
            isAppearanceLightNavigationBars = false  // false = white icons
            // Ensure system bars behavior is consistent across Android versions
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
        
        // Observe notification settings
        lifecycleScope.launch {
            quoteViewModel.isDailyNotificationEnabled.collect { enabled ->
                if (enabled) {
                    NotificationScheduler.scheduleDailyNotification(this@MainActivity)
                } else {
                    NotificationScheduler.cancelDailyNotification(this@MainActivity)
                }
            }
        }
        
        setContent {
            val isDarkMode by quoteViewModel.isDarkMode.collectAsState()
            val view = LocalView.current
            
            SideEffect {
                val window = (view.context as ComponentActivity).window
                // Set icon colors for status bar and navigation bar
                WindowCompat.getInsetsController(window, view)?.apply {
                    isAppearanceLightStatusBars = false  // White icons for dark background
                    isAppearanceLightNavigationBars = false  // White icons for dark background
                }
            }
            
            ConfuciusTheme(darkTheme = isDarkMode) {
                var showSplash by remember { mutableStateOf(true) }
                
                if (showSplash) {
                    AnimatedSplashScreen(
                        onSplashComplete = { showSplash = false }
                    )
                } else {
                    MainScreen(
                        quoteViewModel = quoteViewModel,
                        chatViewModel = chatViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    quoteViewModel: QuoteViewModel,
    chatViewModel: ChatViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, Icons.Default.Home, "Home"),
        BottomNavItem(Screen.Works.route, Icons.AutoMirrored.Filled.MenuBook, "Works"),
        BottomNavItem(Screen.Favorites.route, Icons.Default.Favorite, "Favorites"),
        BottomNavItem(Screen.Chat.route, Icons.AutoMirrored.Filled.Chat, "Chat"),
        BottomNavItem(Screen.Settings.route, Icons.Default.Settings, "Settings")
    )
    
    // Hide bottom bar on Chat screen - users can only go back using back button
    val showBottomBar = currentRoute in bottomNavItems.map { it.route } && currentRoute != Screen.Chat.route
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F1419),  // Deep Greek night
                        Color(0xFF1A1F2E),  // Midnight Aegean
                        Color(0xFF0A0E1A),  // Ancient starry sky
                        Color(0xFF1A1F2E)   // Midnight Aegean
                    )
                )
            )
    ) {
        // Animated gradient background with Greek-inspired colors
        AnimatedGradientBackground(
            modifier = Modifier.fillMaxSize(),
            colors = listOf(
                PremiumColors.AegeanBlue.copy(alpha = 0.08f),
                PremiumColors.OliveGreen.copy(alpha = 0.06f),
                PremiumColors.Terracotta.copy(alpha = 0.05f)
            )
        )
        
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Main content
            NavGraph(
                navController = navController,
                quoteViewModel = quoteViewModel,
                chatViewModel = chatViewModel
            )
            
            // Floating bottom bar overlaid on top
            if (showBottomBar) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    PremiumFloatingBottomBar(
                        items = bottomNavItems,
                        currentRoute = currentRoute ?: Screen.Home.route,
                        onItemClick = { route ->
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    // Pop up to the start destination and save state
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
