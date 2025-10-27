package com.example.ukmskillshare10

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardType as TextKeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ukmskillshare10.data.StudentProfile

@Composable
fun StudentProfileSettingScreen(
    onBackClick: () -> Unit,
    context: android.content.Context
) {
    // Using static data instead of database
    val defaultProfile = remember {
        StudentProfile(
            name = "Armin Rafiqin",
            studentId = "A201010",
            email = "arminrafiqin@gmail.com",
            phoneNumber = "+60 11-63130800",
            course = "Bachelor of Software Engineering (Multimedia)",
            faculty = "Faculty of Information Science & Technology",
            yearOfStudy = "Year 2",
            notificationsEnabled = true,
            language = "English",
            theme = "Light"
        )
    }

    var studentProfile by remember { mutableStateOf(defaultProfile) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showContactHelpDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                text = studentProfile.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF445BA5)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = studentProfile.course,
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
                value = studentProfile.name,
                editable = true,
                onValueChange = {
                    studentProfile = studentProfile.copy(name = it)
                }
            )

            EditableTextField(
                label = "Student ID",
                value = studentProfile.studentId,
                editable = false,
                onValueChange = {}
            )

            EditableTextField(
                label = "Email",
                value = studentProfile.email,
                editable = false,
                onValueChange = {}
            )

            EditableTextField(
                label = "Phone Number",
                value = studentProfile.phoneNumber,
                editable = true,
                keyboardType = TextKeyboardType.Phone,
                onValueChange = {
                    studentProfile = studentProfile.copy(phoneNumber = it)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Academic Information Section
        ProfileSection(
            title = "Academic Information",
            icon = painterResource(id = R.drawable.education)
        ) {
            DropdownField(
                label = "Course",
                value = studentProfile.course,
                options = listOf(
                    "Bachelor of Software Engineering (Multimedia)",
                    "Bachelor of Computer Science",
                    "Bachelor of Information Technology"
                ),
                onValueChange = {
                    studentProfile = studentProfile.copy(course = it)
                }
            )

            DropdownField(
                label = "Faculty",
                value = studentProfile.faculty,
                options = listOf(
                    "Faculty of Information Science & Technology",
                    "Faculty of Engineering & Built Environment",
                    "Faculty of Economics & Business"
                ),
                onValueChange = {
                    studentProfile = studentProfile.copy(faculty = it)
                }
            )

            DropdownField(
                label = "Year of Study",
                value = studentProfile.yearOfStudy,
                options = listOf("Year 1", "Year 2", "Year 3", "Year 4"),
                onValueChange = {
                    studentProfile = studentProfile.copy(yearOfStudy = it)
                }
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
                checked = studentProfile.notificationsEnabled,
                onCheckedChange = {
                    studentProfile = studentProfile.copy(notificationsEnabled = it)
                }
            )

            DropdownField(
                label = "Language",
                value = studentProfile.language,
                options = listOf("English", "Malay", "Chinese"),
                onValueChange = {
                    studentProfile = studentProfile.copy(language = it)
                }
            )

            RadioGroupField(
                label = "Theme",
                options = listOf("Light", "Dark", "System"),
                selectedOption = studentProfile.theme,
                onOptionSelected = {
                    studentProfile = studentProfile.copy(theme = it)
                }
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
                    // Simply show success dialog (no database save)
                    showSuccessDialog = true
                },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Save Changes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bottom Navigation
        BottomNavigationBar()
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
            onValueChange = {},
            label = { Text(label) },
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

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NavItem(icon = painterResource(id = R.drawable.home), label = "Home")
        NavItem(icon = painterResource(id = R.drawable.browse), label = "Browse")
        NavItem(icon = painterResource(id = R.drawable.sessions), label = "Sessions")
        NavItem(icon = painterResource(id = R.drawable.profile), label = "Profile", isSelected = true)
    }
}

@Composable
fun NavItem(
    icon: androidx.compose.ui.graphics.painter.Painter,
    label: String,
    isSelected: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = if (isSelected) Color(0xFF445BA5) else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) Color(0xFF445BA5) else Color.Gray
        )
    }
}

