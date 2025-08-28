# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

This is a Kotlin Multiplatform (KMP) project for a NewsFeed application targeting Android, iOS, and Web platforms. The project follows a clean architecture pattern with a Store-based state management system (inspired by Coffeegram architecture).

## Development Commands

### Build & Run Commands
- **Android**: `./gradlew :composeApp:assembleDebug`
- **Web/WASM**: `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
- **iOS**: Open `iosApp/iosApp.xcworkspace` in Xcode or use `./gradlew :composeApp:embedAndSignAppleFrameworkForXcode`

### Testing
- **Run all tests**: `./gradlew test`
- **Android tests**: `./gradlew :composeApp:testDebugUnitTest`
- **Run single test**: `./gradlew test --tests "ClassName.methodName"`

### Development Tasks  
- **Clean build**: `./gradlew clean`
- **Check dependencies**: `./gradlew dependencies`
- **Build release**: `./gradlew :composeApp:assembleRelease`

## Architecture Overview

### Store Pattern (State Management)
The application uses a custom Store pattern for state management:
- **`InMemoryStore<Intent, State>`**: Abstract base class for all stores
- **`Store<Intent, State>`**: Interface defining store contract
- Each store handles specific domain logic (Auth, News, Theme, Navigation)

Key stores:
- `AuthStore`: User authentication and session management  
- `NewsStore`: News articles data and loading states
- `ThemeStore`: App theming (light/dark mode) preferences
- `NavigationComponent`: Screen navigation using Decompose library

### Navigation Architecture
Uses Arkivanov Decompose for cross-platform navigation:
- Navigation state is serializable and type-safe
- Stack-based navigation with back button support
- Screens: Splash → Login → NewsList → NewsDetail/Profile/Settings

### Project Structure
```
composeApp/src/
├── commonMain/kotlin/org/hacorp/newsfeed/
│   ├── store_lib/          # Store pattern implementation
│   ├── model/              # Store implementations (Auth, News, Theme)
│   ├── pages/              # Screen composables
│   ├── components/         # Reusable UI components  
│   ├── data/               # Data models (NewsArticle, User)
│   ├── animations/         # Animation utilities
│   └── App.kt              # Main app entry point
├── androidMain/kotlin/     # Android-specific code
├── iosMain/kotlin/         # iOS-specific code  
└── wasmJsMain/kotlin/      # Web-specific code
```

### Key Dependencies
- **Compose Multiplatform**: UI framework
- **Decompose**: Navigation and lifecycle management
- **Ktor**: HTTP client for API calls
- **Koin**: Dependency injection
- **Kotlinx Serialization**: JSON serialization
- **Material 3**: Design system

## Development Patterns

### Adding New Screens
1. Create screen composable in `pages/`
2. Add navigation config to `NavigationComponent.Config`
3. Add child type to `NavigationComponent.Child`
4. Update `child()` factory method
5. Add navigation methods if needed

### Creating New Stores
1. Define Intent and State sealed classes/interfaces
2. Extend `InMemoryStore<Intent, State>`
3. Implement `handleIntent(intent: Intent): State`
4. Initialize store in `App.kt`

### Authentication Flow
- Uses demo authentication (any email/password works)
- `AuthStore` manages user session state
- Login required for accessing main app features
- User preferences stored in `User.preferences`

### Theme System
- Material 3 design system with light/dark mode support
- `ThemeStore` manages theme preferences
- System theme detection supported
- Theme state persists through `DarkThemeState` enum

## Mock Data
The application includes sample data for development:
- 3 sample news articles in different categories
- Mock user with preferences
- Realistic article structure with metadata

## Platform-Specific Notes
- **Android**: Uses `MainActivity` with Compose integration
- **iOS**: Uses `MainViewController` for UIKit bridge  
- **Web**: WASM-based with `ComposeViewport`
- All platforms share the same UI code through Compose Multiplatform
