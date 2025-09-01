# Rick & Morty Characters

A simple Android application built with **Kotlin** and **Jetpack Compose** that fetches data from the [Rick and Morty API](https://rickandmortyapi.com/) and displays characters in a paginated list.  

---

## 📱 Features

### Screen 1: Character List
- Displays a paginated list of characters (20 per page).  
- Each character item shows: **Name, Image, Species**.  
- Supports **searching** characters by name.  
- Handles **loading, error, and empty states**.  
- On item tap → navigates to details screen.

### Screen 2: Character Details
- Shows character details: **Name, Image, Species, Status**.

---

## 🛠️ Tech Stack

- **Kotlin** + **Jetpack Compose** for modern declarative UI.  
- **Material 3** for design system.  
- **Hilt** for Dependency Injection.  
- **Retrofit** + **OkHttp** + **Gson** for networking.  
- **Kotlin Coroutines & Flow** for async operations.  
- **DataStore** for local persistence.  
- **Paging 3** for pagination.  
- **Coil** for image loading.  
- **Timber** for logging.  

### 🧪 Testing
- **JUnit4**, **MockK**, **Kluent**, **Turbine**.  

---

## 🏗️ Architecture

The project follows **Clean Architecture** and **Modularization** principles.

- **app** → Application entry point, DI setup, Navigation host, Theming.  

- **core** (shared logic & modules):
  - `common`: utilities, constants, helpers.  
  - `data`: repository implementations.  
  - `dataStore`: persistence with DataStore.  
  - `design`: design system & shared UI components.  
  - `domain`: entities & use cases (business logic).  
  - `model`: DTOs / mappers.  
  - `navigation`: navigation graph and routes.  
  - `network`: Retrofit services and API setup.  

- **feature** (feature-based modules):
  - `home`: character list, pagination, search.  
  - `details`: character details screen.  

This modular structure ensures:
- **Separation of concerns**.  
- **Scalability** for new features.  
- **Reusability** of common components.  
- **Testability** of each layer.  


---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (or newer).  
- Minimum SDK: 24.  
- Target SDK: 35.  

### Build & Run
1. Clone this repository:
   ```bash
   git clone https://github.com/osamasayed585/Rick-Morty-Characters.git
