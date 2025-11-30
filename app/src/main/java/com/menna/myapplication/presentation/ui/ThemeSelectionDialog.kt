package com.example.popcorntime.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.popcorntime.theme.ThemeManager
import com.example.popcorntime.ui.theme.orbitronsFontFamily

@Composable
fun ThemeSelectionDialog(
    onDismiss: () -> Unit
) {
    val isDarkTheme by ThemeManager.isDarkTheme
    val currentColors = ThemeManager.currentColors

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            color = currentColors.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Theme",
                    fontSize = 20.sp,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = currentColors.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                // زر التبديل بين السمتين
                ThemeToggleSwitch()

                Spacer(modifier = Modifier.height(32.dp))

                // أزرار الإجراءات
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = currentColors.secondary
                        )
                    ) {
                        Text(
                            "Close",
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeToggleSwitch() {
    val isDarkTheme by ThemeManager.isDarkTheme
    val currentColors = ThemeManager.currentColors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(if (isDarkTheme) Color(0xFF2A1E2A) else Color(0xFFF0F0F0))
            .clickable { ThemeManager.toggleTheme() },
        contentAlignment = Alignment.Center
    ) {
        // الخلفية المتحركة
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 8.dp)
        ) {
            // زر التبديل المتحرك
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .offset(x = if (isDarkTheme) 110.dp else (-110).dp)
                    .shadow(4.dp, CircleShape, clip = false)
                    .clip(CircleShape)
                    .background(currentColors.primary)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                // أيقونة داخل الزر المتحرك
                if (isDarkTheme) {
                    Icon(
                        Icons.Filled.DarkMode,
                        contentDescription = "Dark Mode",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        Icons.Filled.LightMode,
                        contentDescription = "Light Mode",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // النصوص الثابتة
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // الجانب الأيسر - السمة الفاتحة
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 20.dp)
            ) {
                Icon(
                    Icons.Filled.LightMode,
                    contentDescription = "Light Mode",
                    tint = if (!isDarkTheme) currentColors.primary else currentColors.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Light",
                    color = if (!isDarkTheme) currentColors.primary else currentColors.secondary,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = if (!isDarkTheme) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

            // الجانب الأيمن - السمة الداكنة
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 20.dp)
            ) {
                Text(
                    "Dark",
                    color = if (isDarkTheme) currentColors.primary else currentColors.secondary,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = if (isDarkTheme) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Filled.DarkMode,
                    contentDescription = "Dark Mode",
                    tint = if (isDarkTheme) currentColors.primary else currentColors.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// نسخة بديلة بسيطة من زر التبديل
@Composable
fun SimpleThemeToggle() {
    val isDarkTheme by ThemeManager.isDarkTheme
    val currentColors = ThemeManager.currentColors

    Box(
        modifier = Modifier
            .width(120.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isDarkTheme) Color(0xFF2A1E2A) else Color(0xFFF0F0F0))
            .clickable { ThemeManager.toggleTheme() },
        contentAlignment = Alignment.Center
    ) {
        // زر التبديل المتحرك
        Box(
            modifier = Modifier
                .size(36.dp)
                .offset(x = if (isDarkTheme) 30.dp else (-30).dp)
                .shadow(4.dp, CircleShape, clip = false)
                .clip(CircleShape)
                .background(currentColors.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (isDarkTheme) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                contentDescription = if (isDarkTheme) "Dark Mode" else "Light Mode",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}