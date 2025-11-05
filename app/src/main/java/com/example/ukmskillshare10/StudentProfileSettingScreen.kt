package com.example.ukmskillshare10

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType as TextKeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun StudentProfileSettingScreen(
    onBackClick: () -> Unit,
    onSaved: () -> Unit = {},
    context: android.content.Context
) {
    // Firebase instances
    val auth = remember { FirebaseAuth.getInstance() }
    val db = remember { FirebaseFirestore.getInstance() }
    val coroutineScope = rememberCoroutineScope()
    val currentUser = auth.currentUser
    
    // Local state management
    var name by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }
    var yearOfStudy by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("English") }
    var theme by remember { mutableStateOf("Light") }
    
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showContactHelpDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Load profile data when screen opens
    LaunchedEffect(currentUser?.uid) {
        if (currentUser?.uid != null) {
            isLoading = true
            try {
                val profileDoc = withContext(Dispatchers.IO) {
                    db.collection("studentProfiles")
                        .document(currentUser.uid)
                        .get()
                        .await()
                }
                
                if (profileDoc.exists()) {
                    val data = profileDoc.data
                    name = data?.get("name") as? String ?: ""
                    studentId = data?.get("studentId") as? String ?: ""
                    email = data?.get("email") as? String ?: currentUser.email ?: ""
                    phoneNumber = data?.get("phoneNumber") as? String ?: ""
                    course = data?.get("course") as? String ?: ""
                    faculty = data?.get("faculty") as? String ?: ""
                    yearOfStudy = data?.get("yearOfStudy") as? String ?: ""
                    notificationsEnabled = data?.get("notificationsEnabled") as? Boolean ?: false
                    language = data?.get("language") as? String ?: "English"
                    theme = data?.get("theme") as? String ?: "Light"
                } else {
                    // Initialize with user email if available
                    email = currentUser.email ?: ""
                }
            } catch (e: Exception) {
                errorMessage = "Failed to load profile: ${e.message}"
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header with Back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Text(
                        text = "✕",
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }
                Text(
                    text = "Student Profile",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF445BA5),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Profile Picture Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.personal),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                FloatingActionButton(
                    onClick = { /* TODO: Open image picker */ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp),
                    containerColor = Color.White,
                    contentColor = Color.Black
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Edit Profile Picture",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (name.isNotEmpty()) name else "Student Name",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (name.isNotEmpty()) Color(0xFF445BA5) else Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (course.isNotEmpty()) course else "Select Course",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Personal Information Section
            ProfileSection(
                title = "Personal Information",
                icon = painterResource(id = R.drawable.personal)
            ) {
                EditableTextField(
                    label = "Name",
                    value = name,
                    editable = true,
                    onValueChange = { name = it }
                )

                EditableTextField(
                    label = "Student ID",
                    value = studentId,
                    editable = true,
                    keyboardType = TextKeyboardType.Text,
                    onValueChange = { studentId = it }
                )

                EditableTextField(
                    label = "Email",
                    value = email,
                    editable = true,
                    keyboardType = TextKeyboardType.Email,
                    onValueChange = { email = it }
                )

                EditableTextField(
                    label = "Phone Number",
                    value = phoneNumber,
                    editable = true,
                    keyboardType = TextKeyboardType.Phone,
                    onValueChange = { phoneNumber = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Academic Information Section
            ProfileSection(
                title = "Academic Information",
                icon = painterResource(id = R.drawable.education)
            ) {
                EditableTextField(
                    label = "Course",
                    value = course,
                    editable = true,
                    keyboardType = TextKeyboardType.Text,
                    onValueChange = { course = it }
                )

                DropdownField(
                    label = "Faculty",
                    value = faculty,
                    options = listOf(
                        "FTSM",
                        "FEP",
                        "FUU",
                        "FKAB",
                        "FST",
                        "FPEND",
                        "FSSK",
                        "CITRA",
                        "FPI",
                        "FSK",
                        "FPER",
                        "FGG",
                        "FFAR"
                    ),
                    onValueChange = { faculty = it }
                )

                DropdownField(
                    label = "Year of Study",
                    value = yearOfStudy,
                    options = listOf("Year 1", "Year 2", "Year 3", "Year 4", "Year 5"),
                    onValueChange = { yearOfStudy = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preferences Section
            ProfileSection(
                title = "Preferences",
                icon = painterResource(id = R.drawable.setting)
            ) {
                SwitchField(
                    label = "Notification",
                    description = "Receive alerts and updates",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )

                DropdownField(
                    label = "Language",
                    value = language,
                    options = listOf("English", "Malay", "Chinese"),
                    onValueChange = { language = it }
                )

                RadioGroupField(
                    label = "Theme",
                    options = listOf("Light", "Dark", "System"),
                    selectedOption = theme,
                    onOptionSelected = { theme = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Role Management Section
            ProfileSection(
                title = "Role Management",
                icon = painterResource(id = R.drawable.manage_profile)
            ) {
                ActionButton(
                    text = "Switch Role",
                    onClick = { onBackClick() }, // Navigate back to role selection
                    icon = painterResource(id = R.drawable.setting)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Security & Support Section
            ProfileSection(
                title = "Security & Support",
                icon = painterResource(id = R.drawable.security)
            ) {
                ActionButton(
                    text = "Change Password",
                    onClick = { showPasswordDialog = true },
                    icon = painterResource(id = R.drawable.lock)
                )

                ActionButton(
                    text = "Contact Help",
                    onClick = { showContactHelpDialog = true }
                )

                ActionButton(
                    text = "Delete Account",
                    onClick = { showDeleteDialog = true },
                    isDestructive = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Changes Button
            Button(
                onClick = {
                    if (currentUser?.uid == null) {
                        errorMessage = "User not logged in"
                        return@Button
                    }
                    
                    coroutineScope.launch {
                        try {
                            isLoading = true
                            errorMessage = null
                            
                            val profileData = hashMapOf(
                                "userId" to currentUser.uid,
                                "email" to email,
                                "name" to name,
                                "studentId" to studentId,
                                "phoneNumber" to phoneNumber,
                                "course" to course,
                                "faculty" to faculty,
                                "yearOfStudy" to yearOfStudy,
                                "notificationsEnabled" to notificationsEnabled,
                                "language" to language,
                                "theme" to theme,
                                "updatedAt" to com.google.firebase.Timestamp.now()
                            )
                            
                            withContext(Dispatchers.IO) {
                                db.collection("studentProfiles")
                                    .document(currentUser.uid)
                                    .set(profileData)
                                    .await()
                            }
                            
                            isLoading = false
                            showSuccessDialog = true
                            // Navigate to dashboard after short confirmation
                            onSaved()
                        } catch (e: Exception) {
                            isLoading = false
                            errorMessage = "Failed to save profile: ${e.message}"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isLoading) "Saving..." else "Save Changes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
            
            // Error message
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialogs
    if (showPasswordDialog) {
        ChangePasswordDialog(onDismiss = { showPasswordDialog = false })
    }

    if (showContactHelpDialog) {
        ContactHelpDialog(onDismiss = { showContactHelpDialog = false })
    }

    if (showDeleteDialog) {
        DeleteAccountDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                // Account deletion would happen here
                // For now, just show success dialog
            }
        )
    }

    if (showSuccessDialog) {
        SuccessDialog(onDismiss = { showSuccessDialog = false })
    }
}

@Composable
fun ProfileSection(
    title: String,
    icon: androidx.compose.ui.graphics.painter.Painter,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color(0xFF445BA5),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF445BA5)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        content()
    }
}

@Composable
fun EditableTextField(
    label: String,
    value: String,
    editable: Boolean,
    keyboardType: TextKeyboardType = TextKeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (editable) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        placeholder = { Text("Enter $label") },
        enabled = editable,
        readOnly = !editable,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        trailingIcon = {
            Icon(
                imageVector = if (editable) Icons.Default.Edit else Icons.Default.Lock,
                contentDescription = if (editable) "Editable" else "Locked",
                tint = Color.Gray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    )
}

@Composable
fun DropdownField(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(bottom = 12.dp)
    ) {
        OutlinedTextField(
            readOnly = true,
            value = value,
            onValueChange = { _ -> },
            label = { Text(label) },
            placeholder = { Text("Select $label") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SwitchField(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun RadioGroupField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) }
                )
                Text(
                    text = option,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.painter.Painter? = null,
    isDestructive: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(bottom = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDestructive) Color.White else Color.Gray.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = if (isDestructive) Color.Black else Color.Black
            )
        }
    }
}

@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Change Password",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF445BA5)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            // TODO: Implement password change logic
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF445BA5)
                        )
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun ContactHelpDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Contact Help",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF445BA5),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "For assistance, please contact:",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Email: support@ukmskillshare.edu.my\nPhone: +60 12-345 6789",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF445BA5)
                    )
                ) {
                    Text("Got it")
                }
            }
        }
    }
}

@Composable
fun DeleteAccountDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Delete Account",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Are you sure you want to delete your account? This action cannot be undone.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onConfirm()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Success!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Your profile has been saved successfully.",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF445BA5)
                    )
                ) {
                    Text("OK")
                }
            }
        }
    }
}


