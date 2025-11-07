package com.kreggscode.confuciusquotes.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.geometry.Offset
import com.kreggscode.confuciusquotes.model.Category
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
    AnimatedHomeBackground {
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
    val primary = gradient.firstOrNull() ?: MaterialTheme.colorScheme.primary
    val secondary = gradient.lastOrNull() ?: MaterialTheme.colorScheme.secondary
    val shimmer by rememberInfiniteTransition().animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            primary.copy(alpha = 0.95f),
                            secondary.copy(alpha = 0.9f)
                        )
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.White.copy(alpha = 0.08f))
                    .blur(40.dp)
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.18f),
                                Color.Transparent,
                                Color.White.copy(alpha = 0.16f)
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        ),
                        alpha = (shimmer + 1f) / 6f + 0.05f
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White.copy(alpha = 0.18f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getFeatureTagline(title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp
                    )
                }
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
    
    val primary = gradientColors.firstOrNull() ?: MaterialTheme.colorScheme.primary
    val secondary = gradientColors.lastOrNull() ?: MaterialTheme.colorScheme.secondary

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            primary.copy(alpha = 0.92f),
                            secondary.copy(alpha = 0.88f)
                        )
                    )
                )
                .clip(RoundedCornerShape(28.dp))
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.15f))
            )

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .offset(x = 140.dp, y = (-30).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.White.copy(alpha = 0.28f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = category.icon,
                    fontSize = 54.sp,
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
                        fontSize = 23.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${category.quoteCount} insights",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap to explore wisdom in this theme",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.74f)
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

@Composable
private fun AnimatedHomeBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val basePalette = listOf(
        Color(0xFF0D1B2A),
        Color(0xFF1B263B),
        Color(0xFF415A77)
    )
    val accentPalette = listOf(
        Color(0xFF261C2C),
        Color(0xFF572D6C),
        Color(0xFFF29F05)
    )
    val transition = rememberInfiniteTransition()
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedColors = basePalette.indices.map { index ->
        lerp(basePalette[index], accentPalette[index], progress)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.linearGradient(colors = animatedColors)
                )
        )

        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.12f), Color.Transparent)
                ),
                radius = size.minDimension * 0.8f,
                center = Offset(
                    x = size.width * (0.25f + progress * 0.3f),
                    y = size.height * 0.15f
                )
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFF4D35E).copy(alpha = 0.1f), Color.Transparent)
                ),
                radius = size.minDimension,
                center = Offset(
                    x = size.width * (0.85f - progress * 0.35f),
                    y = size.height * 0.85f
                )
            )
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.12f))
        )

        content()
    }
}

private fun getFeatureTagline(title: String): String = when (title.lowercase()) {
    "about confucius" -> "Follow the path of the Master"
    "affirmations" -> "Center your day with mindful words"
    "works" -> "Study the classic teachings in depth"
    "chat" -> "Converse with a sage in real time"
    else -> "Discover timeless guidance"
}
