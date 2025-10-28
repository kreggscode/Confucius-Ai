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
    onWorksClick: () -> Unit,
    onAffirmationsClick: () -> Unit,
    onChatClick: () -> Unit
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
                        subtitle = "TEACHINGS OF THE GREAT SAGE"
                    )
                }
                // Stunning feature cards
                item {
                    EnhancedFeatureCard(
                        title = "About Confucius",
                        icon = Icons.Default.Person,
                        gradient = listOf(Color(0xFFFFD700), Color(0xFFB8860B)),  // Imperial gold
                        onClick = onAboutClick
                    )
                }
                
                item {
                    EnhancedFeatureCard(
                        title = "Affirmations",
                        icon = Icons.Default.FavoriteBorder,
                        gradient = listOf(Color(0xFFE34234), Color(0xFFDC143C)),  // Cinnabar red
                        onClick = onAffirmationsClick
                    )
                }
                
                item {
                    EnhancedFeatureCard(
                        title = "Works",
                        icon = Icons.Default.MenuBook,
                        gradient = listOf(Color(0xFF00A86B), Color(0xFF2E8B57)),  // Jade green
                        onClick = onWorksClick
                    )
                }
                
                item {
                    EnhancedFeatureCard(
                        title = "Chat",
                        icon = Icons.Default.Chat,
                        gradient = listOf(Color(0xFF4B0082), Color(0xFF483D8B)),  // Scholar indigo
                        onClick = onChatClick
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
        // Confucius-themed Chinese color gradients
        "wisdom" -> listOf(Color(0xFFFFD700), Color(0xFFB8860B))  // Imperial gold
        "freedom" -> listOf(Color(0xFF00A86B), Color(0xFF2E8B57))  // Jade green
        "politics" -> listOf(Color(0xFFE34234), Color(0xFFDC143C))  // Cinnabar red
        "money" -> listOf(Color(0xFFD4AF37), Color(0xFFB8860B))  // Ancient gold
        "knowledge" -> listOf(Color(0xFF4B0082), Color(0xFF483D8B))  // Scholar indigo
        "friendship" -> listOf(Color(0xFFFF8C00), Color(0xFFFF7F50))  // Warm persimmon
        "courage" -> listOf(Color(0xFFDC143C), Color(0xFFB22222))  // Chinese red
        "freedom of speech" -> listOf(Color(0xFF6B8E23), Color(0xFF556B2F))  // Bamboo
        "war & peace" -> listOf(Color(0xFF8B4513), Color(0xFF654321))  // Sandalwood
        "history" -> listOf(Color(0xFF8B7355), Color(0xFF6B5D4F))  // Ancient bronze
        "education" -> listOf(Color(0xFFFFD700), Color(0xFFDAA520))  // Scholar gold
        "justice" -> listOf(Color(0xFF00A86B), Color(0xFF008B5B))  // Jade justice
        "religion" -> listOf(Color(0xFF9B59B6), Color(0xFF8B4789))  // Silk purple
        "time" -> listOf(Color(0xFF708090), Color(0xFF556B7F))  // Stone gray
        "truth" -> listOf(Color(0xFFFFD700), Color(0xFFDAA520))  // Golden truth
        "happiness" -> listOf(Color(0xFFFF6B6B), Color(0xFFFF4757))  // Joyful red
        "philosophy" -> listOf(Color(0xFF8B4513), Color(0xFF654321))  // Wisdom brown
        "death" -> listOf(Color(0xFF2F4F4F), Color(0xFF1C2833))  // Deep ink
        "tolerance" -> listOf(Color(0xFF88B04B), Color(0xFF6B8E23))  // Tea green
        "love" -> listOf(Color(0xFFFF69B4), Color(0xFFFF1493))  // Lotus pink
        "work" -> listOf(Color(0xFFCD7F32), Color(0xFFB8860B))  // Bronze
        "science" -> listOf(Color(0xFF4682B4), Color(0xFF4169E1))  // Sky blue
        "government" -> listOf(Color(0xFFE34234), Color(0xFFDC143C))  // Imperial red
        "morality" -> listOf(Color(0xFF6B8E23), Color(0xFF556B2F))  // Virtue green
        "society" -> listOf(Color(0xFFFF6347), Color(0xFFFF4500))  // Harmony red
        "women" -> listOf(Color(0xFFFFB6C1), Color(0xFFFF69B4))  // Peony pink
        "men" -> listOf(Color(0xFF4682B4), Color(0xFF4169E1))  // Azure blue
        "humanity" -> listOf(Color(0xFFFFD700), Color(0xFFDAA520))  // Benevolence gold
        "success" -> listOf(Color(0xFF00A86B), Color(0xFF2E8B57))  // Prosperity jade
        "art" -> listOf(Color(0xFF9B59B6), Color(0xFF8B4789))  // Artistic silk
        else -> listOf(Color(0xFFFFD700), Color(0xFFB8860B))  // Default imperial gold
    }
}
