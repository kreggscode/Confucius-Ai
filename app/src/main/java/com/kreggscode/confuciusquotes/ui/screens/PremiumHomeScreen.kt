package com.kreggscode.confuciusquotes.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.kreggscode.confuciusquotes.model.Quote
import com.kreggscode.confuciusquotes.ui.components.*
import com.kreggscode.confuciusquotes.ui.theme.*
import com.kreggscode.confuciusquotes.viewmodel.QuoteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumHomeScreen(
    viewModel: QuoteViewModel,
    onQuoteClick: (Quote) -> Unit,
    onCategoryClick: (String) -> Unit,
    onWorkClick: () -> Unit,
    onAboutClick: () -> Unit,
    onQuoteOfDayClick: () -> Unit = {},
    onAffirmationsClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {}
) {
    val allQuotes by viewModel.allQuotes.collectAsState()
    val favoriteQuotes by viewModel.favoriteQuotes.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val scope = rememberCoroutineScope()
    
    var quoteOfTheDay by remember { mutableStateOf<Quote?>(null) }
    var showQuoteOfDay by remember { mutableStateOf(false) }
    var showFullScreenAnimation by remember { mutableStateOf(false) }
    
    // Get quote of the day
    LaunchedEffect(allQuotes) {
        if (allQuotes.isNotEmpty()) {
            val dayOfYear = LocalDateTime.now().dayOfYear
            val index = dayOfYear % allQuotes.size
            quoteOfTheDay = allQuotes[index]
            showQuoteOfDay = true
        }
    }
    
    val categories = remember(allQuotes) {
        allQuotes.map { it.Category }.distinct()
    }
    
    val filteredQuotes = remember(allQuotes, selectedCategory) {
        if (selectedCategory == null) allQuotes
        else allQuotes.filter { it.Category == selectedCategory }
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Greeting Header
            item {
                GreetingHeader()
            }
            
            // Premium Header with animation
            item {
                AnimatedHeader(
                    onTap = { showFullScreenAnimation = true }
                )
            }
            
            // Quote of the Day
            if (quoteOfTheDay != null && showQuoteOfDay) {
                item {
                    QuoteOfTheDayCard(
                        quote = quoteOfTheDay!!,
                        onClick = { onQuoteClick(quoteOfTheDay!!) },
                        onDismiss = { showQuoteOfDay = false }
                    )
                }
            }
            
            // Quick Actions
            item {
                QuickActionsSection(
                    onAboutClick = onAboutClick,
                    onWorksClick = onWorkClick,
                    favoriteCount = favoriteQuotes.size,
                    onQuoteOfDayClick = onQuoteOfDayClick,
                    onAffirmationsClick = onAffirmationsClick,
                    onChatClick = onChatClick,
                    onFavoritesClick = onFavoritesClick
                )
            }
            
            // Categories with horizontal scroll
            if (categories.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🏛️",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                "Philosophical Categories",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = PremiumColors.AncientGold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(3.dp)
                                .padding(top = 4.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            PremiumColors.AncientGold,
                                            PremiumColors.Terracotta,
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            CategoryChip(
                                category = "All",
                                isSelected = selectedCategory == null,
                                onClick = { viewModel.setSelectedCategory(null) },
                                color = PremiumColors.CyberBlue
                            )
                        }
                        items(categories) { category ->
                            CategoryChip(
                                category = category,
                                isSelected = selectedCategory == category,
                                onClick = { viewModel.setSelectedCategory(category) },
                                color = when(categories.indexOf(category) % 8) {
                                    0 -> PremiumColors.OliveGreen
                                    1 -> PremiumColors.Terracotta
                                    2 -> PremiumColors.AncientGold
                                    3 -> PremiumColors.AegeanBlue
                                    4 -> PremiumColors.PurpleRoyal
                                    5 -> PremiumColors.BronzeAge
                                    6 -> PremiumColors.SageGreen
                                    else -> PremiumColors.CrimsonRed
                                }
                            )
                        }
                    }
                }
            }
            
            // Section title with animation
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    PremiumColors.AegeanBlue.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📜",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Timeless Teachings",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = PremiumColors.MarbleWhite
                            )
                        }
                        
                        Box(
                            modifier = Modifier
                                .background(
                                    PremiumColors.OliveGreen.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${filteredQuotes.size} quotes",
                                style = MaterialTheme.typography.labelLarge,
                                color = PremiumColors.AncientGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            // Quotes Grid with staggered animation
            if (filteredQuotes.isEmpty()) {
                item {
                    EmptyStateCard()
                }
            } else {
                itemsIndexed(
                    items = filteredQuotes,
                    key = { _, quote -> quote.id }
                ) { index, quote ->
                    AnimatedQuoteCard(
                        quote = quote,
                        index = index,
                        onClick = { onQuoteClick(quote) },
                        onFavoriteClick = { scope.launch { viewModel.toggleFavorite(quote) } }
                    )
                }
            }
            
            // Bottom spacing for navigation
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        
        // Full-screen animation overlay
        if (showFullScreenAnimation) {
            ConfuciusFullScreenAnimation(
                onAnimationEnd = { showFullScreenAnimation = false }
            )
        }
    }
}

@Composable
fun GreetingHeader() {
    val currentHour = remember { LocalDateTime.now().hour }
    val greeting = when (currentHour) {
        in 0..11 -> "Χαίρετε" // Greek: Greetings (morning)
        in 12..16 -> "Καλημέρα" // Greek: Good day
        in 17..20 -> "Καλησπέρα" // Greek: Good evening
        else -> "Καλή νύχτα" // Greek: Good night
    }
    
    val subtitle = when (currentHour) {
        in 0..11 -> "Begin your journey to wisdom"
        in 12..16 -> "Continue your philosophical pursuit"
        in 17..20 -> "Reflect on the day's insights"
        else -> "Rest with contemplation"
    }
    
    val icon = when (currentHour) {
        in 0..11 -> "🌅"
        in 12..16 -> "☀️"
        in 17..20 -> "🌆"
        else -> "🌙"
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        PremiumColors.OliveGreen.copy(alpha = 0.15f),
                        PremiumColors.Terracotta.copy(alpha = 0.1f),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = icon,
                fontSize = 32.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            
            Column {
                Text(
                    text = greeting,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = PremiumColors.AncientGold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PremiumColors.IvoryParchment.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun AnimatedHeader(
    onTap: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "header")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )
    
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTap() },
        glowColor = PremiumColors.AncientGold.copy(alpha = glowAlpha),
        animateGlow = true
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            PremiumColors.AegeanBlue.copy(alpha = 0.15f),
                            PremiumColors.OliveGreen.copy(alpha = 0.1f),
                            PremiumColors.Terracotta.copy(alpha = 0.15f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(1000f * shimmer, 1000f * shimmer)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Greek temple with enhanced glow
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    // Multi-layered glow effect
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .scale(scale)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        PremiumColors.AncientGold.copy(alpha = glowAlpha * 0.6f),
                                        PremiumColors.Terracotta.copy(alpha = glowAlpha * 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )
                    
                    // Secondary glow
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .scale(scale * 0.9f)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        PremiumColors.AegeanBlue.copy(alpha = glowAlpha * 0.2f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )
                    
                    Text(
                        text = "🏛️",
                        fontSize = 72.sp,
                        modifier = Modifier
                            .scale(scale)
                            .rotate(rotation)
                            .graphicsLayer {
                                shadowElevation = 12.dp.toPx()
                            }
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Confucius branding with enhanced Greek styling
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Confucius",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Black,
                            color = PremiumColors.MarbleWhite,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.graphicsLayer {
                                shadowElevation = 10.dp.toPx()
                            }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "AI",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Black,
                            color = PremiumColors.AncientGold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .graphicsLayer {
                                    shadowElevation = 10.dp.toPx()
                                }
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            PremiumColors.AegeanBlue.copy(alpha = 0.4f),
                                            PremiumColors.OliveGreen.copy(alpha = 0.3f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Greek text with golden shimmer
                    Text(
                        text = "Ἀριστοτέλης",
                        style = MaterialTheme.typography.titleLarge,
                        color = PremiumColors.AncientGold.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.graphicsLayer {
                            shadowElevation = 6.dp.toPx()
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Decorative Greek border
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(2.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        PremiumColors.AncientGold.copy(alpha = 0.6f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "The Philosopher's Path to Wisdom",
                    style = MaterialTheme.typography.bodyLarge,
                    color = PremiumColors.IvoryParchment.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.8.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Greek philosophical quote
                Text(
                    text = "φιλοσοφία • σοφία • ἀρετή",
                    style = MaterialTheme.typography.bodySmall,
                    color = PremiumColors.SageGreen.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}

@Composable
fun QuoteOfTheDayCard(
    quote: Quote,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        PremiumGlassCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            gradientColors = PremiumColors.AuroraGradient
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Dismiss button
                IconButton(
                    onClick = {
                        visible = false
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Quote of the Day",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "\"${quote.Quote}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        lineHeight = 24.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = quote.Work,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = quote.Year.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionsSection(
    onAboutClick: () -> Unit,
    onWorksClick: () -> Unit,
    favoriteCount: Int,
    onQuoteOfDayClick: () -> Unit,
    onAffirmationsClick: () -> Unit,
    onChatClick: () -> Unit,
    onFavoritesClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // First row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Person,
                title = "About\nConfucius",
                gradient = listOf(
                    PremiumColors.AegeanBlue,
                    PremiumColors.PurpleRoyal,
                    PremiumColors.AegeanBlue.copy(alpha = 0.8f)
                ),
                onClick = onAboutClick
            )
            
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.MenuBook,
                title = "Works &\nPapers",
                gradient = listOf(
                    PremiumColors.Terracotta,
                    PremiumColors.AncientGold,
                    PremiumColors.BronzeAge
                ),
                onClick = onWorksClick
            )
            
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Favorite,
                title = "$favoriteCount\nFavorites",
                gradient = listOf(
                    PremiumColors.OliveGreen,
                    PremiumColors.SageGreen,
                    PremiumColors.OliveGreen.copy(alpha = 0.8f)
                ),
                onClick = onFavoritesClick
            )
        }
        
        // Second row with new features
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.AutoAwesome,
                title = "Quote of\nthe Day",
                gradient = listOf(
                    PremiumColors.AncientGold,
                    PremiumColors.BronzeAge,
                    PremiumColors.Terracotta
                ),
                onClick = onQuoteOfDayClick
            )
            
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.SelfImprovement,
                title = "Daily\nAffirmations",
                gradient = listOf(
                    PremiumColors.PurpleRoyal,
                    PremiumColors.CrimsonRed,
                    PremiumColors.Terracotta
                ),
                onClick = onAffirmationsClick
            )
            
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Chat,
                title = "AI\nInsights",
                gradient = listOf(
                    PremiumColors.BronzeAge,
                    PremiumColors.AncientGold.copy(alpha = 0.9f),
                    PremiumColors.BronzeAge.copy(alpha = 0.8f)
                ),
                onClick = onChatClick
            )
        }
    }
}

@Composable
fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "scale"
    )
    
    // Shimmer animation
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )
    
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconScale"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = gradient,
                    start = Offset(shimmerOffset * 200, shimmerOffset * 200),
                    end = Offset((shimmerOffset + 1) * 200, (shimmerOffset + 1) * 200)
                )
            )
            .clickable {
                isPressed = true
                onClick()
                isPressed = false
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Subtle overlay shimmer effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                        start = Offset(shimmerOffset * 200, shimmerOffset * 200),
                        end = Offset((shimmerOffset + 1) * 200, (shimmerOffset + 1) * 200)
                    )
                )
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .scale(iconScale)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )
    
    // Create gradient colors based on the main color
    val gradientColors = when(color) {
        PremiumColors.OliveGreen -> listOf(
            Color(0xFF6B8E23),
            Color(0xFF556B1F),
            Color(0xFF3F4F17)
        )
        PremiumColors.Terracotta -> listOf(
            Color(0xFFE07A5F),
            Color(0xFFD4614A),
            Color(0xFFC14835)
        )
        PremiumColors.AncientGold -> listOf(
            Color(0xFFD4AF37),
            Color(0xFFC19A2E),
            Color(0xFFAE8625)
        )
        PremiumColors.AegeanBlue -> listOf(
            Color(0xFF3D5A80),
            Color(0xFF2F4766),
            Color(0xFF21344D)
        )
        PremiumColors.PurpleRoyal -> listOf(
            Color(0xFF9B59B6),
            Color(0xFF8E44AD),
            Color(0xFF7D3C98)
        )
        PremiumColors.BronzeAge -> listOf(
            Color(0xFFCD7F32),
            Color(0xFFB86F2B),
            Color(0xFFA35F24)
        )
        PremiumColors.SageGreen -> listOf(
            Color(0xFF81B29A),
            Color(0xFF6FA188),
            Color(0xFF5D9076)
        )
        PremiumColors.CrimsonRed -> listOf(
            Color(0xFFDC143C),
            Color(0xFFC41230),
            Color(0xFFAC1024)
        )
        else -> listOf(color, color.copy(alpha = 0.8f), color.copy(alpha = 0.6f))
    }
    
    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = if (isSelected) {
                    Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            color.copy(alpha = 0.25f),
                            color.copy(alpha = 0.15f)
                        )
                    )
                }
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                brush = Brush.linearGradient(
                    colors = if (isSelected) {
                        listOf(
                            Color.White.copy(alpha = 0.5f),
                            color.copy(alpha = 0.8f)
                        )
                    } else {
                        listOf(color.copy(alpha = 0.4f), color.copy(alpha = 0.2f))
                    }
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) Color.White else color.copy(alpha = 0.9f),
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun AnimatedQuoteCard(
    quote: Quote,
    index: Int,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(index) {
        delay(index * 50L)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInHorizontally(
            initialOffsetX = { if (index % 2 == 0) -it else it }
        )
    ) {
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            glowColor = when(index % 4) {
                0 -> PremiumColors.ElectricPurple
                1 -> PremiumColors.NeonPink
                2 -> PremiumColors.CyberBlue
                else -> PremiumColors.QuantumTeal
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "\"${quote.Quote}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (quote.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (quote.isFavorite) Color(0xFFE91E63) else Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = PremiumColors.CyberBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = quote.Work,
                            style = MaterialTheme.typography.labelSmall,
                            color = PremiumColors.CyberBlue,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    Text(
                        text = quote.Category,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateCard() {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        animateGlow = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "🔍",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No quotes found",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "Try selecting a different category",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ConfuciusFullScreenAnimation(
    onAnimationEnd: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        delay(3000) // Animation duration
        visible = false
        delay(300) // Fade out duration
        onAnimationEnd()
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
                .clickable { 
                    scope.launch {
                        visible = false
                        delay(300)
                        onAnimationEnd()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Animated particles/formulas in background
            AnimatedParticles()
            
            // Central Confucius animation
            ConfuciusCentralAnimation()
        }
    }
}

@Composable
fun AnimatedParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    
    // Greek philosophical symbols and letters
    val greekSymbols = listOf(
        "φ", "σ", "ψ", "Ω", "Δ", "Σ", "Π", "Λ", 
        "α", "β", "γ", "θ", "μ", "ε", "ρ", "τ",
        "🏛️", "📜", "⚖️", "🕊️"
    )
    
    // Pre-calculate all animation values
    val rotationValues = greekSymbols.indices.map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(12000 + index * 800, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation$index"
        )
    }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        greekSymbols.forEachIndexed { index, symbol ->
            val angle = (index * 18f) + rotationValues[index].value
            
            val radius = size.minDimension * 0.38f
            val x = center.x + radius * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = center.y + radius * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()
            
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    // Greek-inspired golden and terracotta colors
                    val colorValue = when(index % 4) {
                        0 -> android.graphics.Color.argb(120, 212, 175, 55)  // Ancient gold
                        1 -> android.graphics.Color.argb(100, 224, 122, 95)  // Terracotta
                        2 -> android.graphics.Color.argb(110, 61, 90, 128)   // Aegean blue
                        else -> android.graphics.Color.argb(105, 129, 178, 154) // Sage green
                    }
                    color = colorValue
                    textSize = if (symbol.length > 1) 32f else 44f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                drawText(symbol, x, y, paint)
            }
        }
    }
}

@Composable
fun ConfuciusCentralAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "Confucius")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Glowing circle behind Greek temple with Greek colors
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Outer glow - Ancient Gold
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .scale(scale * 0.95f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Transparent,
                                PremiumColors.AncientGold.copy(alpha = glowAlpha * 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
            
            // Middle glow - Terracotta
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                PremiumColors.Terracotta.copy(alpha = glowAlpha * 0.5f),
                                PremiumColors.AegeanBlue.copy(alpha = glowAlpha * 0.4f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
            
            // Inner glow - Aegean Blue
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .scale(scale * 1.05f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                PremiumColors.AegeanBlue.copy(alpha = glowAlpha * 0.6f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
            
            // Greek temple icon instead of brain
            Text(
                text = "🏛️",
                fontSize = 130.sp,
                modifier = Modifier
                    .scale(scale)
                    .rotate(rotation)
                    .graphicsLayer {
                        shadowElevation = 20.dp.toPx()
                    }
            )
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Confucius AI with Greek styling
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Confucius AI",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Black,
                color = PremiumColors.MarbleWhite,
                modifier = Modifier
                    .scale(scale * 0.75f)
                    .graphicsLayer {
                        shadowElevation = 18.dp.toPx()
                    }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Greek subtitle
            Text(
                text = "Ἀριστοτέλης",
                style = MaterialTheme.typography.headlineMedium,
                color = PremiumColors.AncientGold.copy(alpha = 0.9f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.graphicsLayer {
                    shadowElevation = 10.dp.toPx()
                }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Philosophical tagline
        Text(
            text = "Wisdom Through Philosophy",
            style = MaterialTheme.typography.titleLarge,
            color = PremiumColors.SageGreen,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Greek philosophical virtues
        Text(
            text = "φιλοσοφία • σοφία • ἀρετή",
            style = MaterialTheme.typography.bodyLarge,
            color = PremiumColors.IvoryParchment.copy(alpha = 0.7f),
            fontWeight = FontWeight.Normal,
            letterSpacing = 1.5.sp,
            textAlign = TextAlign.Center
        )
    }
}
