package com.example.popcorntime.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.popcorntime.R
import com.example.popcorntime.ui.theme.orbitronsFontFamily
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class HelloScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val backgroundColor = Color(0xFF100A10)
        val systemUiController = rememberSystemUiController()


        // تغيير لون شريط الحالة والتنقل
        SideEffect {
            systemUiController.setStatusBarColor(backgroundColor, darkIcons = false)
            systemUiController.setNavigationBarColor(backgroundColor, darkIcons = false)
        }

        // ====== مهم جدًا: كل الصفحة داخل Box ======
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {

            // زر Skip أعلى يمين
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 16.dp)   // 40dp لجعلها قريبة من شريط الحالة
            ) {
                Text("Skip", fontSize = 20.sp, color = Color.White,fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light)
                IconButton(
                    onClick = { navigator.push(MainScreen()) }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            // محتوى منتصف الشاشة
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(30.dp))

                Image(
                    painter = painterResource(R.drawable.my_icon),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.FillBounds
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text("Hello", fontFamily = orbitronsFontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 60.sp, color = Color.White)

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    "Welcome To Popcorn Time, where you\n .................... ",
                    fontSize = 20.sp,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = { navigator.push(LoginScreen()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 100.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9B27B0)
                    )
                ) {
                    Text("Login", color = Color.White, fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { navigator.push(RegisterScreen()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 100.dp),
                    shape = RoundedCornerShape(40.dp),
                    border = BorderStroke(2.dp, Color(0xFF9B27B0))
                ) {
                    Text("Register", color = Color.White, fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light)
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text("Sign up using", fontSize = 18.sp, color = Color.White,fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light)

                Spacer(modifier = Modifier.height(30.dp))

//                Row {
//                    Icon(
//                        painter = painterResource(R.drawable.icons8_facebook_circled),
//                        contentDescription = null,
//                        tint = Color.Unspecified
//                    )
//
//                    Spacer(modifier = Modifier.width(30.dp))
//
//                    Icon(
//                        painter = painterResource(R.drawable.icons8_google_plus),
//                        contentDescription = null,
//                        tint = Color.Unspecified
//                    )
//                }
            }
        }
    }
}
