package com.example.popcorntime.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.popcorntime.presentation.viewmodel.LoginViewModel
import com.example.popcorntime.ui.theme.orbitronsFontFamily
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class LoginScreen : Screen {
    @Composable
    override fun Content() {

        val backgroundColor = Color(0xFF100A10)
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(backgroundColor, darkIcons = false)
            systemUiController.setNavigationBarColor(backgroundColor, darkIcons = false)
        }

        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val loginViewModel: LoginViewModel = hiltViewModel()
        val email = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Logo
                Image(
                    painter = painterResource(R.drawable.my_icon),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .shadow(12.dp, RoundedCornerShape(20.dp), clip = false)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Login Title
                Text("Login", fontSize = 40.sp, color = Color.White, fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light)
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
                        focusedIndicatorColor = Color(0xFF9B27B0),
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
                        focusedIndicatorColor = Color(0xFF9B27B0),
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    "Forgot password?",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp),
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(50.dp))

                // Login Button (Gradient)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF9B27B0),
                                    Color(0xFF3974D3)
                                )
                            )
                        )
                        .clickable {
                            loginViewModel.login(
                                email.value,
                                password.value,
                                onLogin = { navigator.push(MainScreen()) },
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
                        Text("Login", color = Color.White, fontSize = 18.sp,fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light)
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

                // Signup Prompt
                Row {
                    Text("Don't have an account?", fontSize = 19.sp, color = Color.White,fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        "sign up",
                        fontSize = 19.sp,
                        fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF9B27B0),
                        modifier = Modifier.clickable { navigator.push(RegisterScreen()) }
                    )
                }
            }
        }
    }
}
