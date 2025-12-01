package com.example.popcorntime.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.popcorntime.R
import com.example.popcorntime.ui.theme.orbitronsFontFamily
import com.example.popcorntime.viewmodel.FirebaseAuthViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class RegisterScreen : Screen {
    @Composable
    override fun Content() {

        val backgroundColor = Color(0xFF080224)
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(backgroundColor, darkIcons = false)
            systemUiController.setNavigationBarColor(backgroundColor, darkIcons = false)
        }

        val navigator = LocalNavigator.currentOrThrow
        val firebaseViewModel: FirebaseAuthViewModel = viewModel()
        val context = LocalContext.current

        val username = rememberSaveable { mutableStateOf("") }
        val email = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo
            Image(
                painter = painterResource(R.drawable.my_icon),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            Text("Create account", fontSize = 38.sp, color = Color.White,fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Light)
            Spacer(modifier = Modifier.height(20.dp))

            // Username Field
            Text("Username", color = Color.White,fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Light, modifier = Modifier.align(Alignment.Start,))
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                placeholder = { Text("Enter your Name", color = Color.Gray,fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = { Icon(Icons.Filled.AccountBox, null, tint = Color.White) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    focusedIndicatorColor = Color(0xFF6C0E9B),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email Field
            Text("Email", color = Color.White,fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Light, modifier = Modifier.align(Alignment.Start))
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                placeholder = { Text("Enter your Email", color = Color.Gray,fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = { Icon(Icons.Filled.Email, null, tint = Color.White) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    focusedIndicatorColor = Color(0xFF6C0E9B),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password Field
            Text("Password", color = Color.White,fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Light, modifier = Modifier.align(Alignment.Start))
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = { Text("Enter your Password", color = Color.Gray,fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = { Icon(Icons.Filled.Lock, null, tint = Color.White) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    focusedIndicatorColor = Color(0xFF6C0E9B),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Sign Up Button (Gradient)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF6C0E9B), Color(0xFF3974D3)))
                    )
                    .clickable {
                        firebaseViewModel.register(
                            email.value,
                            password.value,
                            onRegister = { navigator.push(MainScreen()) },
                            onError = {
                                Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.ExitToApp, null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Sign Up", color = Color.White, fontSize = 18.sp,fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light)
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Login Prompt
            Row {
                Text("Already have an account?", fontSize = 18.sp, color = Color.White,fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light)
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    "sign in",
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 18.sp,
                    color = Color(0xFF6C0E9B),
                    modifier = Modifier.clickable { navigator.push(LoginScreen()) }
                )
            }
        }
    }
}
