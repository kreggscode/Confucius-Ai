package com.kreggscode.confuciusquotes.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kreggscode.confuciusquotes.ui.components.GlassmorphicCard
import com.kreggscode.confuciusquotes.ui.theme.PremiumColors
import androidx.compose.ui.platform.LocalContext
import com.kreggscode.confuciusquotes.data.WorksDataLoader
import kotlinx.coroutines.launch

enum class WorkCategory {
    MAJOR_WORKS, ESSAYS, LETTERS, PAPERS
}

data class WorkItem(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val year: String,
    val icon: String,
    val summary: String,
    val category: WorkCategory
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfuciusWorksScreen(
    onBackClick: () -> Unit,
    onWorkClick: (WorkItem) -> Unit,
    onChatClick: (String) -> Unit,
    initialCategory: WorkCategory? = null
) {
    // Use rememberSaveable with custom saver to preserve category selection across navigation
    // This prevents ClassCastException in release builds with ProGuard
    var selectedCategory by rememberSaveable(
        stateSaver = object : Saver<WorkCategory?, String> {
            override fun restore(value: String): WorkCategory? {
                return when (value) {
                    "MAJOR_WORKS" -> WorkCategory.MAJOR_WORKS
                    "ESSAYS" -> WorkCategory.ESSAYS
                    "LETTERS" -> WorkCategory.LETTERS
                    "PAPERS" -> WorkCategory.PAPERS
                    "null" -> null
                    else -> null
                }
            }
            override fun SaverScope.save(value: WorkCategory?): String {
                return value?.name ?: "null"
            }
        }
    ) { mutableStateOf<WorkCategory?>(initialCategory) }
    val context = LocalContext.current
    val dataLoader = remember { WorksDataLoader(context) }
    val coroutineScope = rememberCoroutineScope()
    
    var majorWorks by remember { mutableStateOf<List<WorkItem>>(emptyList()) }
    var essays by remember { mutableStateOf<List<WorkItem>>(emptyList()) }
    var letters by remember { mutableStateOf<List<WorkItem>>(emptyList()) }
    var papers by remember { mutableStateOf<List<WorkItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Handle back button press
    BackHandler(enabled = selectedCategory != null) {
        selectedCategory = null
    }
    
    // Load data from JSON
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val loadedMajorWorks = dataLoader.loadMajorWorks()
            majorWorks = loadedMajorWorks.map { work ->
                WorkItem(
                    id = work.id,
                    title = work.title,
                    subtitle = work.subtitle,
                    year = work.year,
                    icon = work.icon,
                    summary = work.summary,
                    category = WorkCategory.MAJOR_WORKS
                )
            }
            
            val loadedEssays = dataLoader.loadEssays()
            essays = loadedEssays.map { essay ->
                WorkItem(
                    id = essay.id,
                    title = essay.title,
                    subtitle = "",
                    year = essay.year,
                    icon = essay.icon,
                    summary = essay.summary,
                    category = WorkCategory.ESSAYS
                )
            }
            
            val loadedLetters = dataLoader.loadLetters()
            letters = loadedLetters.map { letter ->
                WorkItem(
                    id = letter.id,
                    title = letter.title,
                    subtitle = letter.recipient,
                    year = letter.date,
                    icon = letter.icon,
                    summary = letter.summary,
                    category = WorkCategory.LETTERS
                )
            }
            
            val loadedPapers = dataLoader.loadPapers()
            papers = loadedPapers.map { paper ->
                WorkItem(
                    id = paper.id,
                    title = paper.title,
                    subtitle = paper.journal ?: "",
                    year = paper.year,
                    icon = paper.icon,
                    summary = paper.summary,
                    category = WorkCategory.PAPERS
                )
            }
            
            isLoading = false
        }
    }
    
    // Force dark theme colors for this screen
    val backgroundColor = PremiumColors.DeepSpace
    val textColor = Color.White
    
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
            )
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PremiumColors.ElectricPurple)
            }
            return@Box
        }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Confucius's Works",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            // Header
            item {
                HeaderCard()
            }
            
            // Category Selector
            item {
                CategorySelector(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }
            
            // Show content based on selected category
            when (selectedCategory) {
                WorkCategory.MAJOR_WORKS -> {
                    item {
                        SectionHeader(
                            title = "Major Works",
                            subtitle = "Revolutionary papers that changed physics",
                            icon = "⚡"
                        )
                    }
                    items(majorWorks) { work ->
                        WorkItemCard(
                            work = work,
                            onClick = { onWorkClick(work) },
                            onChatClick = { onChatClick(work.title) }
                        )
                    }
                }
                WorkCategory.ESSAYS -> {
                    item {
                        SectionHeader(
                            title = "Essays",
                            subtitle = "Confucius's philosophical writings",
                            icon = "📝"
                        )
                    }
                    items(essays) { work ->
                        WorkItemCard(
                            work = work,
                            onClick = { onWorkClick(work) },
                            onChatClick = { onChatClick(work.title) }
                        )
                    }
                }
                WorkCategory.LETTERS -> {
                    item {
                        SectionHeader(
                            title = "Letters",
                            subtitle = "Personal correspondence that shaped history",
                            icon = "✉️"
                        )
                    }
                    items(letters) { work ->
                        WorkItemCard(
                            work = work,
                            onClick = { onWorkClick(work) },
                            onChatClick = { onChatClick(work.title) }
                        )
                    }
                }
                WorkCategory.PAPERS -> {
                    item {
                        SectionHeader(
                            title = "Scientific Papers",
                            subtitle = "Published research that defined modern physics",
                            icon = "📄"
                        )
                    }
                    items(papers) { work ->
                        WorkItemCard(
                            work = work,
                            onClick = { onWorkClick(work) },
                            onChatClick = { onChatClick(work.title) }
                        )
                    }
                }
                null -> {
                    // Show all categories overview
                    item {
                        CategoryOverviewCard(
                            title = "Major Works",
                            count = majorWorks.size,
                            icon = "⚡",
                            color = PremiumColors.ElectricPurple,
                            onClick = { selectedCategory = WorkCategory.MAJOR_WORKS }
                        )
                    }
                    item {
                        CategoryOverviewCard(
                            title = "Essays",
                            count = essays.size,
                            icon = "📝",
                            color = PremiumColors.CyberBlue,
                            onClick = { selectedCategory = WorkCategory.ESSAYS }
                        )
                    }
                    item {
                        CategoryOverviewCard(
                            title = "Letters",
                            count = letters.size,
                            icon = "✉️",
                            color = PremiumColors.NeonPink,
                            onClick = { selectedCategory = WorkCategory.LETTERS }
                        )
                    }
                    item {
                        CategoryOverviewCard(
                            title = "Scientific Papers",
                            count = papers.size,
                            icon = "📄",
                            color = PremiumColors.QuantumGold,
                            onClick = { selectedCategory = WorkCategory.PAPERS }
                        )
                    }
                }
            }
            
            }
        }
    }
}

@Composable
private fun HeaderCard() {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                PremiumColors.ElectricPurple.copy(alpha = 0.2f),
                                PremiumColors.CyberBlue.copy(alpha = 0.2f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📚",
                    style = MaterialTheme.typography.displayMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Explore Confucius's Legacy",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Dive into the revolutionary works, essays, letters, and papers that changed our understanding of the universe",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun CategorySelector(
    selectedCategory: WorkCategory?,
    onCategorySelected: (WorkCategory?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CategoryChip(
            label = "All",
            isSelected = selectedCategory == null,
            onClick = { onCategorySelected(null) }
        )
        CategoryChip(
            label = "Major Works",
            isSelected = selectedCategory == WorkCategory.MAJOR_WORKS,
            onClick = { onCategorySelected(WorkCategory.MAJOR_WORKS) }
        )
        CategoryChip(
            label = "Essays",
            isSelected = selectedCategory == WorkCategory.ESSAYS,
            onClick = { onCategorySelected(WorkCategory.ESSAYS) }
        )
        CategoryChip(
            label = "Letters",
            isSelected = selectedCategory == WorkCategory.LETTERS,
            onClick = { onCategorySelected(WorkCategory.LETTERS) }
        )
        CategoryChip(
            label = "Papers",
            isSelected = selectedCategory == WorkCategory.PAPERS,
            onClick = { onCategorySelected(WorkCategory.PAPERS) }
        )
    }
}

@Composable
private fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        PremiumColors.ElectricPurple
    } else {
        Color.White.copy(alpha = 0.1f)
    }
    
    val textColor = if (isSelected) {
        Color.White
    } else {
        Color.White.copy(alpha = 0.9f)
    }
    
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        modifier = Modifier.height(40.dp),
        border = if (!isSelected) BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)) else null
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = textColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    icon: String
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineMedium
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun WorkItemCard(
    work: WorkItem,
    onClick: () -> Unit,
    onChatClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header with icon and year
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    // Animated icon
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        PremiumColors.ElectricPurple.copy(alpha = 0.2f),
                                        PremiumColors.CyberBlue.copy(alpha = 0.2f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = work.icon,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Text(
                            text = work.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (work.subtitle.isNotEmpty()) {
                            Text(
                                text = work.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Year badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PremiumColors.QuantumGold.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = work.year,
                        style = MaterialTheme.typography.labelMedium,
                        color = PremiumColors.QuantumGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Summary
            Text(
                text = work.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Read More button
                Surface(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    color = PremiumColors.ElectricPurple,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Read More",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // AI Insights button
                Surface(
                    onClick = onChatClick,
                    shape = RoundedCornerShape(12.dp),
                    color = PremiumColors.CyberBlue,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Insights",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryOverviewCard(
    title: String,
    count: Int,
    icon: String,
    color: Color,
    onClick: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.08f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    color.copy(alpha = 0.3f),
                                    color.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
                
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "$count items available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
