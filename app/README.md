# 🎬 Movie Tracker and Watchlist App

## 📌 Project Overview
The **Movie Tracker and Watchlist App** is an Android application that allows users to explore movies, manage personal watchlists, track watched movies, and share reviews and feedback.  
The app is designed to provide a smooth, user-friendly experience while demonstrating the use of modern Android development practices.

---

## 🎯 Features

### 🔐 User Authentication
- User registration and login
- Persistent login using local preferences
- Profile management (Edit Name, DOB, Country)

### 🎥 Movie Browsing
- Browse trending and popular movies
- Search movies by keyword and genre
- View detailed movie information including:
    - Poster
    - Genre
    - Release date
    - IMDb rating
    - Plot summary
    - Cast and crew
    - Box office details

### ⭐ Watchlist Management
- Add movies to **Watch Later**
- Mark movies as **Watched**
- Prevent duplicate entries
- Switch from Watch Later → Watched
- Local storage for offline access

### 📝 Ratings & Reviews
- Rate movies (1–5 stars)
- Write reviews
- Each user can submit one review per movie
- Reviews are visible to all users
- Display average rating and review list

### 💬 Feedback & Issue Reporting
- Submit feedback or report issues
- View previously submitted feedback
- Feedback stored securely in Firebase

### 👤 Profile Management
- View user details
- Edit profile with animated UI
- Date of Birth picker
- Data synced with local storage and Firebase

### ▶ Movie Trailer
- Watch movie trailers via YouTube search intent

---

## 🛠 Technologies Used

### Android & UI
- Kotlin
- Jetpack Compose
- Material 3 Design
- Navigation Compose

### Backend & Data
- Firebase Realtime Database
- SQLite (Room Database)
- SharedPreferences (UserPrefs)

### APIs & Libraries
- OMDb API – Movie data
- Coil – Image loading
- Firebase – User data, reviews, feedback

---

## 🧱 Architecture & Design
- Modular and reusable composables
- MVVM-friendly structure
- Separation of UI, data, and logic
- Background threading for network operations
- Clean navigation flow using NavHost

---

## 💾 Data Persistence Strategy
- **Firebase Realtime Database**
    - User profiles
    - Movie reviews and ratings
    - User feedback and issues
- **Room Database**
    - Watch Later movies
    - Watched movies
- **SharedPreferences**
    - Login state
    - User name, email, DOB, country

---

## 🚀 How to Run the Project

1. Clone or download the project
2. Open the project in **Android Studio**
3. Ensure internet connection is available
4. Add your OMDb API key in the constants file
5. Run the app on an emulator or physical device

---

## 📧 Contact Information

**Developer:** Vijay Sai Boya  
**Student Number:** S3493864  
**Email:** vijaysaiboya864@gmail.com


