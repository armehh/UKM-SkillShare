package com.example.ukmskillshare10

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TutorProfileSettingScreen(
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    context: android.content.Context
) {
    // Firebase instances
    val auth = remember { FirebaseAuth.getInstance() }
    val db = remember { FirebaseFirestore.getInstance() }
    val coroutineScope = rememberCoroutineScope()
    val currentUser = auth.currentUser
    
    // Local state management
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf(listOf<String>()) }
    var pricePerHour by remember { mutableStateOf("") }
    var allowNegotiable by remember { mutableStateOf(true) }
    
    var newSkill by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // availability simple toggles
    data class DayAvail(var enabled: Boolean, var slot: String)
    var availability by remember {
        mutableStateOf(
            listOf(
                DayAvail(false, "Not Available"),
                DayAvail(false, "Not Available"),
                DayAvail(false, "Not Available"),
                DayAvail(false, "Not Available"),
                DayAvail(false, "Not Available"),
                DayAvail(false, "Not Available"),
                DayAvail(false, "Not Available")
            )
        )
    }
    
    // Load profile data when screen opens
    LaunchedEffect(currentUser?.uid) {
        if (currentUser?.uid != null) {
            isLoading = true
            try {
                val profileDoc = withContext(Dispatchers.IO) {
                    db.collection("tutorProfiles")
                        .document(currentUser.uid)
                        .get()
                        .await()
                }
                
                if (profileDoc.exists()) {
                    val data = profileDoc.data
                    name = data?.get("name") as? String ?: ""
                    email = data?.get("email") as? String ?: currentUser.email ?: ""
                    phoneNumber = data?.get("phoneNumber") as? String ?: ""
                    skills = (data?.get("skills") as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                    pricePerHour = data?.get("pricePerHour") as? String ?: ""
                    allowNegotiable = data?.get("allowNegotiable") as? Boolean ?: true
                    
                    // Load availability
                    val availabilityData = data?.get("availability") as? List<Map<String, Any>>
                    if (availabilityData != null) {
                        availability = availabilityData.mapIndexed { index, dayData ->
                            DayAvail(
                                enabled = dayData["enabled"] as? Boolean ?: false,
                                slot = dayData["slot"] as? String ?: "Not Available"
                            )
                        }
                    }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) { Text("✕", fontSize = 24.sp, color = Color.Black) }
            Text(
                text = "Tutor Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF445BA5),
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Header avatar
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.personal),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(Modifier.height(8.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = if (name.isNotEmpty()) name else "Tutor Name",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (name.isNotEmpty()) Color(0xFF445BA5) else Color.Gray
            )
            Text(
                text = if (skills.isNotEmpty()) skills.joinToString(" & ") + " Tutor" else "Select Skills",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(16.dp))

        // Personal Info
        SectionCard(title = "Personal Info", iconRes = R.drawable.manage_profile) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Skills
        SectionCard(title = "Skills", iconRes = R.drawable.skills) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                skills.forEach { s -> 
                    AssistChip(
                        onClick = {
                            // Remove skill
                            skills = skills.filter { it != s }
                        },
                        label = { Text(s) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newSkill,
                    onValueChange = { newSkill = it },
                    label = { Text("Add new skill") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (newSkill.isNotBlank()) {
                        skills = skills + newSkill.trim()
                        newSkill = ""
                    }
                }) { Text("Add") }
            }
        }

        // Availability
        SectionCard(title = "Availability", iconRes = R.drawable.sessions) {
            val days = listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
            availability.forEachIndexed { index, day ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(days[index], fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(day.slot, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(end = 12.dp))
                        Switch(checked = day.enabled, onCheckedChange = {
                            availability = availability.toMutableList().also { it[index] = it[index].copy(enabled = !it[index].enabled) }
                        })
                    }
                }
            }
        }

        // Rate Settings
        SectionCard(title = "Rate Settings", iconRes = R.drawable.booking) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("RM", modifier = Modifier.padding(end = 8.dp))
                OutlinedTextField(
                    value = pricePerHour,
                    onValueChange = { 
                        val filteredValue = it.filter { ch -> ch.isDigit() }
                        pricePerHour = filteredValue
                    },
                    modifier = Modifier.width(120.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = allowNegotiable, 
                    onCheckedChange = { 
                        allowNegotiable = it
                    }
                )
                Text("Allow negotiable rates")
            }
        }

        // Role Management
        SectionCard(title = "Role Management", iconRes = R.drawable.manage_profile) {
            TextButton(onClick = { onBackClick() }) { Text("Switch Role") }
        }

        // Security & Support
        SectionCard(title = "Security & Support", iconRes = R.drawable.security) {
            TextButton(onClick = { }) { Text("Change Password") }
            TextButton(onClick = { }) { Text("Contact Help") }
            TextButton(onClick = { }, colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)) { Text("Delete Account") }
        }

        Spacer(Modifier.height(16.dp))

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
                        
                        val availabilityData = availability.map { day ->
                            mapOf(
                                "enabled" to day.enabled,
                                "slot" to day.slot
                            )
                        }
                        
                        val profileData = hashMapOf(
                            "userId" to currentUser.uid,
                            "email" to email,
                            "name" to name,
                            "phoneNumber" to phoneNumber,
                            "skills" to skills,
                            "pricePerHour" to pricePerHour,
                            "allowNegotiable" to allowNegotiable,
                            "availability" to availabilityData,
                            "updatedAt" to com.google.firebase.Timestamp.now()
                        )
                        
                        withContext(Dispatchers.IO) {
                            db.collection("tutorProfiles")
                                .document(currentUser.uid)
                                .set(profileData)
                                .await()
                        }
                        
                        isLoading = false
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
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) { 
            Text(
                text = if (isLoading) "Saving..." else "Save Changes",
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

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SectionCard(title: String, iconRes: Int, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF445BA5))
        }
        Spacer(Modifier.height(8.dp))
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))) {
            Column(modifier = Modifier.padding(16.dp), content = content)
        }
    }
}

@Composable
private fun Chip(text: String) {
    Box(
        modifier = Modifier
            .border(1.dp, Color(0xFF2E3A74), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 12.sp, color = Color(0xFF2E3A74))
    }
}


