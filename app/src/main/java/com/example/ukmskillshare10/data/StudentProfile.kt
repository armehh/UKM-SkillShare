package com.example.ukmskillshare10.data

data class StudentProfile(
    val id: Long = 0,
    
    // Personal Information
    val name: String = "",
    val studentId: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    
    // Academic Information
    val course: String = "",
    val faculty: String = "",
    val yearOfStudy: String = "",
    
    // Preferences
    val notificationsEnabled: Boolean = false,
    val language: String = "English",
    val theme: String = "Light",
    
    // Profile picture path (optional)
    val profilePicturePath: String = ""
)

