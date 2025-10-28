package com.kreggscode.confuciusquotes.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import com.kreggscode.confuciusquotes.model.Category
import com.kreggscode.confuciusquotes.ui.components.GlassCard
import com.kreggscode.confuciusquotes.ui.components.MorphismCard
import com.kreggscode.confuciusquotes.ui.components.GlassmorphicHeader
import com.kreggscode.confuciusquotes.viewmodel.QuoteViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: QuoteViewModel,
    onCategoryClick: (String) -> Unit,
    onQuoteClick: (Int) -> Unit,
    onAboutClick: () -> Unit,
    onWorksClick: () -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    val allQuotes by viewModel.allQuotes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Edge-to-edge design without Scaffold for full control
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),  // Deep navy - Confucius theme
                        Color(0xFF16213E),  // Darker blue
                        Color(0xFF0F3460)   // Rich blue
                    )
                )
            )
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp,
                    bottom = 100.dp  // Navigation bar space
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Glassmorphic animated header
                item(
                    span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }
                ) {
                    val greeting = getConfuciusGreeting()
                    GlassmorphicHeader(
                        title = greeting,
                        subtitle = "INSIGHTS & WISDOM FROM THE PHILOSOPHER"
                    )
                }
                // Stunning feature cards
                item {
                    EnhancedFeatureCard(
                        title = "About Confucius",
                        icon = Icons.Default.Person,
                        gradient = listOf(Color(0xFF2E7D32), Color(0xFF1B5E20)),  // Confucius green
                        onClick = onAboutClick
                    )
                }
                
                item {
                    EnhancedFeatureCard(
                        title = "Works & Papers",
                        icon = Icons.Default.MenuBook,
                        gradient = listOf(Color(0xFF6A1B9A), Color(0xFF4A148C)),  // Confucius purple
                        onClick = onWorksClick
                    )
                }
                
                // Categories with enhanced design
                items(categories) { category ->
                    EnhancedCategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category.name) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnhancedFeatureCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = gradient[0].copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradient + gradient[1].copy(alpha = 0.8f)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnhancedCategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    val gradientColors = getConfuciusCategoryGradient(category.name)
    
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = gradientColors[0].copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = category.icon,
                    fontSize = 56.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${category.quoteCount} insights",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.95f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun getConfuciusGreeting(): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    
    return when (hour) {
        in 5..11 -> "Good Morning, Seeker of Wisdom"
        in 12..16 -> "Good Afternoon, Student of Philosophy"
        in 17..20 -> "Good Evening, Lover of Knowledge"
        else -> "Welcome, Philosopher"
    }
}

@Composable
private fun getConfuciusCategoryGradient(categoryName: String): List<Color> {
    return when (categoryName.lowercase()) {
        // Confucius-themed earthy and philosophical gradients
        "wisdom" -> listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))  // Deep Green
        "freedom" -> listOf(Color(0xFF5E35B1), Color(0xFF4527A0))  // Royal Purple
        "politics" -> listOf(Color(0xFF6A1B9A), Color(0xFF4A148C))  // Deep Purple
        "money" -> listOf(Color(0xFFD84315), Color(0xFFBF360C))  // Burnt Orange
        "knowledge" -> listOf(Color(0xFF1565C0), Color(0xFF0D47A1))  // Deep Blue
        "friendship" -> listOf(Color(0xFFE65100), Color(0xFFBF360C))  // Warm Orange
        "courage" -> listOf(Color(0xFFC62828), Color(0xFFB71C1C))  // Deep Red
        "freedom of speech" -> listOf(Color(0xFF00838F), Color(0xFF006064))  // Teal
        "war & peace" -> listOf(Color(0xFF424242), Color(0xFF212121))  // Dark Gray
        "history" -> listOf(Color(0xFF4E342E), Color(0xFF3E2723))  // Brown
        "education" -> listOf(Color(0xFF7B1FA2), Color(0xFF6A1B9A))  // Purple
        "justice" -> listOf(Color(0xFF01579B), Color(0xFF004D40))  // Deep Blue-Teal
        "religion" -> listOf(Color(0xFF880E4F), Color(0xFF4A148C))  // Magenta-Purple
        "time" -> listOf(Color(0xFF00695C), Color(0xFF004D40))  // Dark Teal
        "truth" -> listOf(Color(0xFFF57C00), Color(0xFFE65100))  // Amber-Orange
        "happiness" -> listOf(Color(0xFFFFB300), Color(0xFFFF8F00))  // Golden Yellow
        "philosophy" -> listOf(Color(0xFF283593), Color(0xFF1A237E))  // Indigo
        "death" -> listOf(Color(0xFF37474F), Color(0xFF263238))  // Blue Gray
        "tolerance" -> listOf(Color(0xFF0097A7), Color(0xFF00838F))  // Cyan
        "love" -> listOf(Color(0xFFAD1457), Color(0xFF880E4F))  // Pink
        "work" -> listOf(Color(0xFFEF6C00), Color(0xFFE65100))  // Orange
        "science" -> listOf(Color(0xFF0277BD), Color(0xFF01579B))  // Light Blue
        "government" -> listOf(Color(0xFF5E35B1), Color(0xFF4527A0))  // Purple
        "morality" -> listOf(Color(0xFF558B2F), Color(0xFF33691E))  // Olive Green
        "society" -> listOf(Color(0xFFD32F2F), Color(0xFFC62828))  // Red
        "women" -> listOf(Color(0xFFC2185B), Color(0xFFAD1457))  // Pink
        "men" -> listOf(Color(0xFF1976D2), Color(0xFF1565C0))  // Blue
        "humanity" -> listOf(Color(0xFF8E24AA), Color(0xFF7B1FA2))  // Purple
        "success" -> listOf(Color(0xFF388E3C), Color(0xFF2E7D32))  // Green
        "art" -> listOf(Color(0xFF7B1FA2), Color(0xFF6A1B9A))  // Purple
        else -> listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))  // Default Confucius green
    }
}
