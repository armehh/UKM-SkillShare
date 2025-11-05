package com.example.ukmskillshare10

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ukmskillshare10.data.StudentProfileRepository
import com.google.firebase.firestore.FirebaseFirestore

class StudentProfileViewModelFactory(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentProfileViewModel::class.java)) {
            val repository = StudentProfileRepository(firestore)
            return StudentProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

