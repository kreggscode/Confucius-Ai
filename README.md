# 智 Confucius Wisdom

<div align="center">

![Confucius Wisdom](https://img.shields.io/badge/Confucius-Wisdom-D4AF37?style=for-the-badge)
![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-100%25-7F52FF?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack-Compose-4285F4?style=for-the-badge&logo=jetpackcompose)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**Ancient Chinese Wisdom • Timeless Teachings**

[Download on Google Play](https://play.google.com/store/apps/details?id=com.kreggscode.confuciusquotes) • [View Documentation](https://kreggscode.github.io/Confucius-Ai/)

</div>

---

## 📖 About

Confucius Wisdom is a beautifully designed Android application that brings the timeless teachings of Confucius (Kong Fuzi, 551-479 BCE) to modern mobile devices. Immerse yourself in ancient Chinese philosophy with an elegant, premium user interface featuring authentic quotes, AI-powered conversations, and daily inspiration.

### ✨ Key Features

- **📚 Extensive Quote Library** - Hundreds of authentic Confucian quotes organized by themes
- **🤖 AI-Powered Chat** - Engage in philosophical conversations with an AI embodiment of Confucius
- **📖 The Analects** - Access complete works and classical Chinese philosophical texts
- **⭐ Daily Wisdom** - Receive daily notifications with carefully selected quotes
- **❤️ Favorites Collection** - Save and organize your favorite teachings
- **🎨 Premium Design** - Chinese aesthetic with gold/bronze colors and glassmorphic UI
- **🌙 Dark Mode** - Elegant dark theme optimized for reading
- **🔔 Smart Notifications** - Customizable daily quote reminders

---

## 🎨 Screenshots

<div align="center">

| Splash Screen | Home | Chat with Confucius |
|:---:|:---:|:---:|
| ![Splash](screenshots/splash.png) | ![Home](screenshots/home.png) | ![Chat](screenshots/chat.png) |

| Quotes Library | Works | Settings |
|:---:|:---:|:---:|
| ![Quotes](screenshots/quotes.png) | ![Works](screenshots/works.png) | ![Settings](screenshots/settings.png) |

</div>

---

## 🛠️ Tech Stack

### Architecture & Design Patterns
- **MVVM (Model-View-ViewModel)** - Clean architecture with separation of concerns
- **Repository Pattern** - Abstracted data layer for flexibility
- **Dependency Injection** - Manual DI for lightweight architecture

### Technologies
- **Kotlin** - 100% Kotlin codebase
- **Jetpack Compose** - Modern declarative UI framework
- **Material Design 3** - Latest Material Design guidelines
- **Coroutines & Flow** - Asynchronous programming and reactive streams
- **Room Database** - Local data persistence with KSP
- **DataStore** - Type-safe data storage for preferences
- **WorkManager** - Background task scheduling for notifications
- **Retrofit + OkHttp** - Network communication
- **Pollinations AI** - AI-powered conversational interface

### UI Components
- **GlassmorphicCard** - Frosted glass effect with blur
- **AnimatedGradientButton** - Spring-animated gradient buttons
- **PremiumFloatingBottomBar** - Glass effect navigation
- **MorphismCard** - Neumorphic design elements
- **Custom Animations** - Smooth transitions and spring physics

---

## 🎯 Design Philosophy

### Color Palette
```kotlin
// Primary Colors - Ancient Chinese Aesthetic
Gold:   #D4AF37  // Imperial gold
Bronze: #8B7355  // Ancient bronze
Navy:   #1A1A2E  // Deep night
Blue:   #16213E  // Midnight blue
Royal:  #0F3460  // Royal blue
```

### Key Design Principles
- **Chinese Aesthetic** - Gold and bronze colors inspired by ancient Chinese art
- **Glassmorphism** - Modern frosted glass effects with subtle transparency
- **Edge-to-Edge** - Immersive full-screen experience
- **Spring Animations** - Natural, physics-based motion
- **Typography** - Clean, readable fonts optimized for philosophical content

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17 or higher
- Android SDK 26+ (minimum) / 36 (target)
- Kotlin 2.2.10+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/kreggscode/Confucius-Ai.git
cd Confucius-Ai
```

2. **Open in Android Studio**
```bash
# Open Android Studio and select "Open an Existing Project"
# Navigate to the cloned directory
```

3. **Build the project**
```bash
./gradlew build
```

4. **Run on device/emulator**
```bash
./gradlew installDebug
```

### Configuration

The app uses **KSP (Kotlin Symbol Processing)** instead of deprecated KAPT:

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.2.10-1.0.29"
}

dependencies {
    ksp("androidx.room:room-compiler:2.8.2")
}
```

---

## 📱 App Structure

```
app/
├── src/main/
│   ├── java/com/kreggscode/confuciusquotes/
│   │   ├── data/              # Data layer
│   │   │   ├── QuoteRepository.kt
│   │   │   ├── QuoteDatabase.kt
│   │   │   ├── PollinationsApiService.kt
│   │   │   └── PreferencesManager.kt
│   │   ├── model/             # Data models
│   │   │   ├── Quote.kt
│   │   │   └── Category.kt
│   │   ├── ui/                # UI layer
│   │   │   ├── screens/       # Compose screens
│   │   │   ├── components/    # Reusable components
│   │   │   └── theme/         # Theme & styling
│   │   ├── viewmodel/         # ViewModels
│   │   ├── navigation/        # Navigation graph
│   │   └── notifications/     # Notification system
│   ├── assets/                # JSON data files
│   └── res/                   # Resources
└── docs/                      # Documentation website
```

---

## 🤖 AI Integration

The app uses **Pollinations AI** for conversational features:

```kotlin
private val systemPrompt = """
    You are Confucius (Kong Fuzi), the ancient Chinese philosopher and teacher.
    You lived from 551 BCE to 479 BCE during the Spring and Autumn period.
    Your teachings focus on ren (benevolence), li (ritual), yi (righteousness),
    xiao (filial piety), and zhong (loyalty).
    Respond with wisdom about ethics, relationships, governance, and personal development.
"""
```

**API Configuration:**
- **Endpoint:** `https://text.pollinations.ai/openai`
- **Model:** OpenAI-compatible
- **Temperature:** 1.0 (balanced creativity)
- **No API Key Required** - Free to use

---

## 📊 Features Breakdown

### 1. Quote Library
- 500+ authentic Confucian quotes
- Organized by categories (Wisdom, Virtue, Learning, Relationships, etc.)
- Search functionality
- Share quotes as images or text

### 2. AI Chat
- Context-aware conversations
- Maintains chat history
- Fallback responses for offline mode
- Clear chat functionality

### 3. Daily Notifications
- Customizable notification time
- Random quote selection
- Silent hours support
- Enable/disable in settings

### 4. Favorites System
- Save unlimited quotes
- Organize by custom categories
- Export favorites
- Sync across devices (future feature)

### 5. Works & Biography
- Complete Analects
- Historical context
- Timeline of Confucius's life
- Major philosophical concepts explained

---

## 🔧 Development

### Building for Release

```bash
# Generate signed APK
./gradlew assembleRelease

# Generate App Bundle for Play Store
./gradlew bundleRelease
```

### Code Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable names
- Document complex logic
- Keep functions small and focused

### Testing
```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 KreggsCode

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📞 Contact & Support

- **Developer:** KreggsCode
- **Email:** kreg9da@gmail.com
- **Google Play:** [Confucius Wisdom](https://play.google.com/store/apps/details?id=com.kreggscode.confuciusquotes)
- **Website:** [Documentation](https://kreggscode.github.io/Confucius-Ai/)

---

## 🙏 Acknowledgments

- **Confucius** - For timeless wisdom that continues to inspire millions
- **Pollinations AI** - For providing free AI API access
- **Material Design** - For comprehensive design guidelines
- **Jetpack Compose** - For modern Android UI development
- **Open Source Community** - For amazing libraries and tools

---

## 📈 Roadmap

- [ ] Multi-language support (Chinese, Japanese, Korean)
- [ ] Offline AI responses
- [ ] Widget for home screen quotes
- [ ] Quote of the day widget
- [ ] Social sharing improvements
- [ ] Cloud sync for favorites
- [ ] Audio narration of quotes
- [ ] Interactive philosophy lessons
- [ ] Community features

---

<div align="center">

**"It does not matter how slowly you go as long as you do not stop."**

*— Confucius*

---

Made with ❤️ by [KreggsCode](https://github.com/kreggscode)

⭐ Star this repo if you find it helpful!

</div>
