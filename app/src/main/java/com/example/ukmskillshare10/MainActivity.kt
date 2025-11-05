package com.example.ukmskillshare10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ukmskillshare10.ui.theme.UKMSkillShare10Theme
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        
        setContent {
            UKMSkillShare10Theme {
                AppContent()
            }
        }
    }
}

@Composable
fun ErrorScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "App Error",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Something went wrong. Please check the logs for details.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AppContent() {
    var currentScreen by remember { mutableStateOf("login") }
    var showLearnMoreDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    when (currentScreen) {
        "login" -> LoginScreen(
            onLoginClick = { currentScreen = "welcome" },
            onSignupClick = { currentScreen = "registration" }
        )
        "registration" -> RegistrationScreen(
            onRegistrationSuccess = { currentScreen = "login" },
            onBackToLogin = { currentScreen = "login" }
        )
        "welcome" -> WelcomeScreen(
            onGetStartedClick = { currentScreen = "role_selection" }
        )
        "role_selection" -> RoleSelectionScreen(
            onBackClick = { currentScreen = "welcome" },
            onStudentClick = { currentScreen = "student_profile" },
            onTutorClick = { currentScreen = "tutor_profile" },
            onLearnMoreClick = { showLearnMoreDialog = true }
        )
        "student_profile" -> StudentProfileSettingScreen(
            onBackClick = { currentScreen = "role_selection" },
            onSaved = { currentScreen = "dashboard" },
            context = context
        )
        "dashboard" -> MainScreenWithNavigation(
            currentScreen = "home",
            onNavigate = { screen -> 
                when (screen) {
                    "home" -> currentScreen = "dashboard"
                    "profile" -> currentScreen = "student_profile"
                    "role_selection" -> currentScreen = "role_selection"
                    else -> currentScreen = "dashboard" // Default to dashboard for browse/sessions
                }
            },
            context = context
        )
        "tutor_profile" -> TutorProfileSettingScreen(
            onBackClick = { currentScreen = "role_selection" },
            onSaved = { currentScreen = "tutor_dashboard" },
            context = context
        )
        "tutor_dashboard" -> TutorMainScreenWithNavigation(
            currentScreen = "home",
            onNavigate = { screen -> 
                when (screen) {
                    "home" -> currentScreen = "tutor_dashboard"
                    "profile" -> currentScreen = "tutor_profile"
                    "role_selection" -> currentScreen = "role_selection"
                    else -> currentScreen = "tutor_dashboard" // Default to tutor dashboard for browse/sessions
                }
            },
            context = context
        )
    }

    if (showLearnMoreDialog) {
        LearnMoreDialog(
            onDismiss = { showLearnMoreDialog = false }
        )
    }
}

@Composable
fun WelcomeScreen(onGetStartedClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "SkillShare Logo",
            modifier = Modifier
                .size(400.dp)
                .padding(bottom = 16.dp)
        )

        // Welcome Text
        Text(
            text = "Welcome to SkillShare",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Description Text
        Text(
            text = "Connect with fellow students, share your expertise, and learn new skills from peers at Universiti Kebangsaan Malaysia.",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Get Started Button
        Button(
            onClick = onGetStartedClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF445BA5)
            )
        ) {
            Text(
                text = "Get Started",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Composable
fun RoleSelectionScreen(
    onBackClick: () -> Unit,
    onStudentClick: () -> Unit,
    onTutorClick: () -> Unit,
    onLearnMoreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBackClick) {
                Text(
                    text = "✕",
                    fontSize = 24.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Student Option
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Student",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF445BA5),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "Learn new skills and get guidance from tutors who share their expertise.",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = onStudentClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF445BA5)
                    )
                ) {
                    Text(
                        text = "Continue as Student",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Tutor Option
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tutor",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF445BA5),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "Share your knowledge, teach students, and earn rewards for your sessions.",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = onTutorClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF445BA5)
                    )
                ) {
                    Text(
                        text = "Continue as Tutor",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Learn More Section
        Text(
            text = "Not sure which role to choose?",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            onClick = onLearnMoreClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF445BA5)
            )
        ) {
            Text(
                text = "Learn More",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun LearnMoreDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Student vs Tutor",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF445BA5),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Student:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "• Access learning resources and materials\n• Find tutors for specific subjects\n• Schedule learning sessions\n• Track your progress and achievements",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "Tutor:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "• Share your expertise and knowledge\n• Create learning materials\n• Connect with students who need help\n• Earn rewards for teaching sessions",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF445BA5)
                    )
                ) {
                    Text(
                        text = "Got it!",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreenWithNavigation(
    currentScreen: String,
    onNavigate: (String) -> Unit,
    context: android.content.Context
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Main content area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Space for bottom navigation
        ) {
            when (currentScreen) {
                "home" -> DashboardScreen(
                    onBrowseClick = { onNavigate("browse") },
                    onBookingsClick = { onNavigate("sessions") },
                    onFeedbackClick = { /* TODO: Implement feedback */ },
                    onHistoryClick = { /* TODO: Implement history */ }
                )
                "browse" -> BrowseScreen()
                "sessions" -> SessionsScreen()
                "profile" -> StudentProfileSettingScreen(
                    onBackClick = { onNavigate("role_selection") },
                    onSaved = { onNavigate("home") },
                    context = context
                )
            }
        }
        
        // Bottom Navigation Bar positioned at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
        }
    }
}

@Composable
fun TutorMainScreenWithNavigation(
    currentScreen: String,
    onNavigate: (String) -> Unit,
    context: android.content.Context
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Main content area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Space for bottom navigation
        ) {
            when (currentScreen) {
                "home" -> TutorDashboardScreen()
                "browse" -> BrowseScreen()
                "sessions" -> SessionsScreen()
                "profile" -> TutorProfileSettingScreen(
                    onBackClick = { onNavigate("role_selection") },
                    onSaved = { onNavigate("home") },
                    context = context
                )
            }
        }
        
        // Bottom Navigation Bar positioned at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
        }
    }
}

@Composable
fun BrowseScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Browse Skills",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF445BA5)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Discover available skills and tutors",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SessionsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sessions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF445BA5)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Manage your learning sessions",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    UKMSkillShare10Theme {
        WelcomeScreen(onGetStartedClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun RoleSelectionScreenPreview() {
    UKMSkillShare10Theme {
        RoleSelectionScreen(
            onBackClick = {},
            onStudentClick = {},
            onTutorClick = {},
            onLearnMoreClick = {}
        )
    }
}