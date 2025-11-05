package com.example.ukmskillshare10

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ukmskillshare10.data.StudentProfile
import com.example.ukmskillshare10.data.StudentProfileRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentProfileViewModel(
    private val repository: StudentProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentProfileUiState())
    val uiState: StateFlow<StudentProfileUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    fun start(uid: String) {
        if (_uiState.value.userId == uid && observeJob != null) return

        _uiState.update { it.copy(userId = uid, isLoading = true, error = null) }

        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            repository
                .observeProfile(uid)
                .catch { throwable ->
                    _uiState.update { state ->
                        state.copy(isLoading = false, error = throwable.message)
                    }
                }
                .collect { profile ->
                    _uiState.update { state ->
                        state.copy(
                            profile = profile ?: StudentProfile(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun onProfileChange(updated: StudentProfile) {
        _uiState.update { state -> state.copy(profile = updated, hasUnsavedChanges = true) }
    }

    fun saveProfile() {
        val uid = _uiState.value.userId ?: return
        val profile = _uiState.value.profile

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            runCatching {
                repository.saveProfile(uid, profile)
            }.onSuccess {
                _uiState.update { it.copy(isSaving = false, hasUnsavedChanges = false) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isSaving = false, error = throwable.message) }
            }
        }
    }
}

data class StudentProfileUiState(
    val userId: String? = null,
    val profile: StudentProfile = StudentProfile(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val hasUnsavedChanges: Boolean = false,
    val error: String? = null
)

