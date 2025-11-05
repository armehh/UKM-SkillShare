package com.example.ukmskillshare10

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun RegistrationScreen(
    onRegistrationSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var matricNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Ensure Firebase is initialized
    val firebaseApp = remember { 
        try {
            FirebaseApp.getInstance()
        } catch (e: IllegalStateException) {
            null
        }
    }

    val auth = remember { 
        if (firebaseApp != null) {
            FirebaseAuth.getInstance(firebaseApp)
        } else {
            FirebaseAuth.getInstance()
        }
    }
    val db = remember { FirebaseFirestore.getInstance() }
    val coroutineScope = rememberCoroutineScope()

    fun validateEmail(email: String): Boolean {
        return email.endsWith("@siswa.ukm.edu.my", ignoreCase = true)
    }

    fun validateForm(): String {
        if (email.isBlank()) {
            return "Email is required"
        }
        if (!validateEmail(email)) {
            return "Email must end with @siswa.ukm.edu.my"
        }
        if (matricNumber.isBlank()) {
            return "Matric number is required"
        }
        if (password.isBlank()) {
            return "Password is required"
        }
        if (password.length < 6) {
            return "Password must be at least 6 characters"
        }
        if (password != confirmPassword) {
            return "Passwords do not match"
        }
        return ""
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Card(
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(max = 340.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "SkillShare Logo",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "Register",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF7047DE),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        errorMessage = ""
                    },
                    label = { Text("Email (@siswa.ukm.edu.my)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    isError = email.isNotBlank() && !validateEmail(email)
                )

                // Matric Number field
                OutlinedTextField(
                    value = matricNumber,
                    onValueChange = { 
                        matricNumber = it
                        errorMessage = ""
                    },
                    label = { Text("Matric Number") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                )

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { 
                        password = it
                        errorMessage = ""
                    },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Text(
                            text = if (passwordVisible) "Hide" else "Show",
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { passwordVisible = !passwordVisible }
                        )
                    }
                )

                // Confirm Password field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { 
                        confirmPassword = it
                        errorMessage = ""
                    },
                    label = { Text("Confirm Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Text(
                            text = if (confirmPasswordVisible) "Hide" else "Show",
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { confirmPasswordVisible = !confirmPasswordVisible }
                        )
                    },
                    isError = confirmPassword.isNotBlank() && password != confirmPassword
                )

                // Error message
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                // Register Button
                Button(
                    onClick = {
                        val validationError = validateForm()
                        if (validationError.isNotEmpty()) {
                            errorMessage = validationError
                            return@Button
                        }

                        isLoading = true
                        errorMessage = ""

                        // Create user in Firebase Auth
                        coroutineScope.launch {
                            try {
                                val result = withContext(Dispatchers.IO) {
                                    auth.createUserWithEmailAndPassword(email, password).await()
                                }
                                
                                // Save additional user data to Firestore
                                val userData = hashMapOf(
                                    "email" to email,
                                    "matricNumber" to matricNumber,
                                    "userId" to result.user?.uid
                                )

                                withContext(Dispatchers.IO) {
                                    db.collection("users")
                                        .document(result.user?.uid ?: "")
                                        .set(userData)
                                        .await()
                                }

                                // Navigate to login on success
                                isLoading = false
                                onRegistrationSuccess()
                            } catch (e: Exception) {
                                isLoading = false
                                errorMessage = when {
                                    e.message?.contains("email address is already in use") == true -> 
                                        "Email is already registered"
                                    e.message?.contains("weak password") == true -> 
                                        "Password is too weak"
                                    e.message?.contains("CONFIGURATION_NOT_FOUND") == true -> 
                                        "Firebase Auth is not enabled. Please enable Authentication in Firebase Console."
                                    e.message?.contains("network") == true -> 
                                        "Network error. Please check your internet connection."
                                    else -> "Registration failed: ${e.message ?: "Unknown error"}"
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    enabled = !isLoading,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7047DE)
                    )
                ) {
                    Text(text = if (isLoading) "Registering..." else "REGISTER")
                }

                // Back to Login link
                Text(
                    text = "Already have an account? Login",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7047DE),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable { onBackToLogin() }
                )
            }
        }
    }
}

