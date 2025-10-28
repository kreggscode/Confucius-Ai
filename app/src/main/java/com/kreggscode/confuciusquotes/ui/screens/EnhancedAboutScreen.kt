package com.kreggscode.confuciusquotes.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.kreggscode.confuciusquotes.ui.components.*
import com.kreggscode.confuciusquotes.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedAboutScreen(
    onBackClick: () -> Unit,
    onWorkClick: ((String) -> Unit)? = null
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Biography", "Works", "Legacy", "Timeline", "Policies")
    
    // Handle back button - if on another tab, go to Biography first, then exit
    BackHandler(enabled = selectedTab != 0) {
        if (selectedTab != 0) {
            selectedTab = 0
        } else {
            onBackClick()
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
            )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "About Confucius",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Tabs
                PrimaryScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    edgePadding = 16.dp
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
                
                // Content
                when (selectedTab) {
                    0 -> BiographyTab()
                    1 -> WorksTab(onWorkClick = onWorkClick)
                    2 -> LegacyTab()
                    3 -> TimelineTab()
                    4 -> PoliciesTab()
                }
            }
        }
    }
}

@Composable
fun BiographyTab() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = PremiumColors.ElectricPurple,
                animateGlow = true
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🏯", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Confucius",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "551 BCE - 479 BCE",
                        style = MaterialTheme.typography.titleMedium,
                        color = PremiumColors.ImperialYellow
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "The Great Teacher & Founder of Confucianism",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        item {
            InfoCard(
                title = "Early Life",
                icon = "👶",
                content = "Born Kong Qiu in 551 BCE in the State of Lu (modern-day Shandong Province, China) during the Spring and Autumn period. Father died when he was three years old. Raised in poverty by his mother, he worked various jobs while dedicating himself to learning ancient Chinese traditions and texts."
            )
        }
        
        item {
            InfoCard(
                title = "Teaching Career (521-479 BCE)",
                icon = "🎓",
                content = "Began teaching around age 30, accepting students from all social classes - a revolutionary approach. First teacher in Chinese history to democratize education, requiring only tuition regardless of birth. Taught over 3,000 students, with 72 becoming renowned scholars."
            )
        }
        
        item {
            InfoCard(
                title = "Government Service & Travels",
                icon = "👑",
                content = "Served briefly in government positions in the State of Lu. Spent 14 years traveling various Chinese states, seeking rulers who would implement his philosophy of moral governance. Promoted rule by moral example rather than force."
            )
        }
        
        item {
            InfoCard(
                title = "Private School & Classical Editing",
                icon = "🏯",
                content = "Established the first private school in Chinese history around 521 BCE. In his later years, focused on teaching and editing classical Chinese texts including the Book of Poetry, Book of Documents, and Book of Rites, preserving ancient wisdom for future generations."
            )
        }
        
        item {
            InfoCard(
                title = "Final Years",
                icon = "📜",
                content = "Returned to the State of Lu in his later years, dedicating himself to teaching and compiling classical texts. Died in 479 BCE at age 72, reportedly saddened by the state of society. His teachings, compiled in the Analects by his disciples, became the foundation of East Asian civilization."
            )
        }
    }
}

@Composable
fun WorksTab(onWorkClick: ((String) -> Unit)? = null) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HighlightCard(
                title = "Ren (Benevolence)",
                subtitle = "The Highest Virtue",
                description = "Confucius's central concept of humaneness, compassion, and love for others. Ren encompasses all virtues and is the foundation of moral cultivation and harmonious relationships.",
                gradient = listOf(PremiumColors.JadeGreen, PremiumColors.TeaGreen)
            )
        }
        
        item {
            Text(
                "Major Works",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        val works = listOf(
            Work("The Analects (Lunyu)", "Compiled posthumously", "Collection of Confucius's sayings and conversations with disciples - the primary source of his teachings", "⭐", null),
            Work("Book of Poetry (Shijing)", "Edited ~500 BCE", "Anthology of 305 Chinese poems and songs that Confucius edited and used for moral instruction", "📜", null),
            Work("Book of Documents (Shujing)", "Edited ~500 BCE", "Collection of political speeches and documents from ancient Chinese rulers", "📚", null),
            Work("Book of Rites (Liji)", "Edited ~500 BCE", "Manual of ritual and ceremonial practices for proper social conduct and harmony", "🎭", null),
            Work("Book of Changes (I Ching)", "Commentary ~500 BCE", "Ancient divination text with Confucius's commentaries on change and transformation", "☯️", null),
            Work("Spring and Autumn Annals", "Compiled ~500 BCE", "Chronicle of the State of Lu, traditionally attributed to Confucius", "📖", null)
        )
        
        items(works) { work ->
            WorkCard(work = work, onClick = {
                work.id?.let { id -> onWorkClick?.invoke(id) }
            })
        }
    }
}

@Composable
fun LegacyTab() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            InfoCard(
                title = "Philosophical Impact",
                icon = "🧠",
                content = "Confucianism became China's official philosophy for over 2,000 years. Shaped governmental systems of China, Korea, Japan, and Vietnam. Created ethical frameworks for personal and social moral development still influential today."
            )
        }
        
        item {
            InfoCard(
                title = "Educational Revolution",
                icon = "🔬",
                content = "First to establish private education accessible to all social classes in China. Democratized learning and created merit-based social mobility. Established the Chinese examination system that shaped bureaucracy for centuries."
            )
        }
        
        item {
            InfoCard(
                title = "Cultural Preservation",
                icon = "📚",
                content = "Preserved and transmitted ancient Chinese classical literature. Edited the Five Classics that became foundation of Chinese education. His emphasis on learning and self-cultivation shaped East Asian educational values."
            )
        }
        
        item {
            InfoCard(
                title = "Social Harmony",
                icon = "☯️",
                content = "Emphasized filial piety as foundation of social order. Created framework of Five Relationships that structured East Asian society. Promoted harmony through proper conduct and moral example."
            )
        }
        
        item {
            InfoCard(
                title = "Modern Relevance",
                icon = "💡",
                content = "Confucian values continue to influence East Asian business practices and education. His birthday celebrated as Teachers' Day across Asia. UNESCO recognizes him as one of the ten greatest thinkers in human history."
            )
        }
    }
}

@Composable
fun TimelineTab() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val timeline = listOf(
            TimelineEvent("551 BCE", "Born in Zou, State of Lu, China", "👶"),
            TimelineEvent("548 BCE", "Father dies; raised in poverty", "💔"),
            TimelineEvent("532 BCE", "Marries at age 19", "💑"),
            TimelineEvent("521 BCE", "Begins teaching; establishes private school", "🎓"),
            TimelineEvent("501 BCE", "Appointed to government position in Lu", "👑"),
            TimelineEvent("497 BCE", "Begins 14-year journey across Chinese states", "🚶"),
            TimelineEvent("484 BCE", "Returns to Lu; focuses on teaching", "🏯"),
            TimelineEvent("483 BCE", "Edits classical texts and compiles teachings", "📚"),
            TimelineEvent("481 BCE", "Completes Spring and Autumn Annals", "📜"),
            TimelineEvent("479 BCE", "Dies at age 72 in State of Lu", "⭐")
        )
        
        items(timeline) { event ->
            TimelineCard(event)
        }
    }
}

data class Work(
    val title: String,
    val year: String,
    val description: String,
    val icon: String,
    val id: String? = null
)

data class TimelineEvent(
    val year: String,
    val event: String,
    val icon: String
)

@Composable
fun InfoCard(title: String, icon: String, content: String) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = PremiumColors.CyberBlue
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                content,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun HighlightCard(
    title: String,
    subtitle: String,
    description: String,
    gradient: List<Color>
) {
    PremiumGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradientColors = gradient
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun WorkCard(work: Work, onClick: (() -> Unit)? = null) {
    val modifier = if (onClick != null && work.id != null) {
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    } else {
        Modifier.fillMaxWidth()
    }
    
    GlassmorphicCard(
        modifier = modifier,
        glowColor = if (work.id != null) PremiumColors.ElectricPurple else PremiumColors.ElectricPurple.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(work.icon, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        work.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        work.year,
                        style = MaterialTheme.typography.labelMedium,
                        color = PremiumColors.CyberBlue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        work.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            
            if (work.id != null) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View details",
                    tint = PremiumColors.ElectricPurple,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun TimelineCard(event: TimelineEvent) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(PremiumColors.ElectricPurple),
                contentAlignment = Alignment.Center
            ) {
                Text(event.icon, fontSize = 20.sp)
            }
            if (event != TimelineEvent("322 BC", "Dies in Chalcis at age 62", "⭐")) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(PremiumColors.ElectricPurple.copy(alpha = 0.3f))
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        GlassmorphicCard(
            modifier = Modifier.weight(1f),
            glowColor = PremiumColors.CyberBlue
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    event.year,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = PremiumColors.CyberBlue
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    event.event,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun PoliciesTab() {
    var selectedPolicy by rememberSaveable { mutableStateOf<String?>(null) }
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (selectedPolicy != null) {
            // Back button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPolicy = null }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PremiumColors.ElectricPurple
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Back to Policies",
                        style = MaterialTheme.typography.titleMedium,
                        color = PremiumColors.ElectricPurple,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Show selected policy content
            if (selectedPolicy == "privacy") {
                privacyPolicyContent()
            } else if (selectedPolicy == "terms") {
                termsAndConditionsContent()
            }
        } else {
            // Show policy selection
            item {
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = PremiumColors.ElectricPurple,
                    animateGlow = true
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📋", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Policies & Legal",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Read our privacy policy and terms of service",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            item {
                PolicyCard(
                    title = "Privacy Policy",
                    icon = "🔒",
                    description = "Learn how we protect your data and respect your privacy",
                    onClick = { selectedPolicy = "privacy" }
                )
            }
            
            item {
                PolicyCard(
                    title = "Terms & Conditions",
                    icon = "📜",
                    description = "Read the terms of service for using Confucius AI",
                    onClick = { selectedPolicy = "terms" }
                )
            }
            
            item {
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = PremiumColors.CyberBlue
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("ℹ️", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "App Version",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "1.0",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PolicyCard(
    title: String,
    icon: String,
    description: String,
    onClick: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        glowColor = PremiumColors.CyberBlue
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(icon, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View",
                tint = PremiumColors.ElectricPurple,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

fun LazyListScope.privacyPolicyContent() {
    item {
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "🔒 Privacy Policy",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Last Updated: October 18, 2025",
                    style = MaterialTheme.typography.labelMedium,
                    color = PremiumColors.CyberBlue
                )
            }
        }
    }
    
    item {
        PolicySection(
            title = "1. Information We Collect",
            content = "Confucius AI is designed with privacy in mind. We collect minimal information:\n\n• Favorites Data: Your saved favorite quotes are stored locally on your device\n• App Preferences: Settings like dark mode and notification preferences\n• Usage Analytics: Anonymous data about app crashes and performance to improve the app"
        )
    }
    
    item {
        PolicySection(
            title = "2. Information We Don't Collect",
            content = "We respect your privacy and do NOT collect:\n\n• Personal identification information (name, email, phone number)\n• Location data\n• Contacts or photos\n• Any data that can personally identify you"
        )
    }
    
    item {
        PolicySection(
            title = "3. Data Storage",
            content = "All your data (favorites, preferences) is stored locally on your device. We do not store your personal data on external servers. Your information stays with you."
        )
    }
    
    item {
        PolicySection(
            title = "4. Third-Party Services",
            content = "Confucius AI may use Google Play Services for app distribution and updates, and Android WorkManager for scheduling daily notifications. These services have their own privacy policies."
        )
    }
    
    item {
        PolicySection(
            title = "5. Children's Privacy",
            content = "Confucius AI is suitable for all ages. We do not knowingly collect personal information from children under 13. The app does not require any personal information to function."
        )
    }
    
    item {
        PolicySection(
            title = "6. Your Rights",
            content = "You have the right to:\n\n• Access your data (all stored locally on your device)\n• Delete your data (uninstall the app or clear app data)\n• Disable notifications at any time\n• Clear cache from the app settings"
        )
    }
    
    item {
        PolicySection(
            title = "7. Contact",
            content = "If you have any questions about this Privacy Policy, please contact us at: kreg9da@gmail.com"
        )
    }
}

fun LazyListScope.termsAndConditionsContent() {
    item {
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "📜 Terms & Conditions",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Last Updated: October 18, 2025",
                    style = MaterialTheme.typography.labelMedium,
                    color = PremiumColors.CyberBlue
                )
            }
        }
    }
    
    item {
        PolicySection(
            title = "1. Acceptance of Terms",
            content = "By downloading, installing, or using Confucius AI, you agree to be bound by these Terms and Conditions. If you do not agree to these terms, please do not use the app."
        )
    }
    
    item {
        PolicySection(
            title = "2. License to Use",
            content = "We grant you a limited, non-exclusive, non-transferable, revocable license to use Confucius AI for personal, non-commercial purposes. You may not:\n\n• Modify, reverse engineer, or decompile the app\n• Remove any copyright or proprietary notices\n• Use the app for any illegal purposes\n• Attempt to gain unauthorized access to the app's systems"
        )
    }
    
    item {
        PolicySection(
            title = "3. Content Ownership",
            content = "All quotes, texts, and content related to Confucius's works are in the public domain. The app's design, code, and original content are © 2025 Kreggscode. All rights reserved."
        )
    }
    
    item {
        PolicySection(
            title = "4. User Conduct",
            content = "You agree to use Confucius AI responsibly and in accordance with all applicable laws. You will not use the app to:\n\n• Harass, abuse, or harm others\n• Distribute malware or harmful code\n• Violate any intellectual property rights\n• Engage in any fraudulent activity"
        )
    }
    
    item {
        PolicySection(
            title = "5. Disclaimer of Warranties",
            content = "Confucius AI is provided \"as is\" without warranties of any kind, either express or implied. We do not guarantee that the app will be error-free, secure, or uninterrupted."
        )
    }
    
    item {
        PolicySection(
            title = "6. Limitation of Liability",
            content = "To the maximum extent permitted by law, Kreggscode shall not be liable for any indirect, incidental, special, consequential, or punitive damages arising from your use of Confucius AI."
        )
    }
    
    item {
        PolicySection(
            title = "7. Changes to Terms",
            content = "We reserve the right to modify these Terms and Conditions at any time. Continued use of the app after changes constitutes acceptance of the modified terms."
        )
    }
    
    item {
        PolicySection(
            title = "8. Termination",
            content = "We may terminate or suspend your access to Confucius AI at any time, without prior notice, for conduct that we believe violates these Terms or is harmful to other users."
        )
    }
    
    item {
        PolicySection(
            title = "9. Governing Law",
            content = "These Terms shall be governed by and construed in accordance with applicable laws, without regard to conflict of law provisions."
        )
    }
    
    item {
        PolicySection(
            title = "10. Contact Information",
            content = "For questions about these Terms and Conditions, please contact us at: kreg9da@gmail.com"
        )
    }
}

@Composable
fun PolicySection(title: String, content: String) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = PremiumColors.CyberBlue.copy(alpha = 0.3f)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PremiumColors.QuantumGold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 22.sp
            )
        }
    }
}
