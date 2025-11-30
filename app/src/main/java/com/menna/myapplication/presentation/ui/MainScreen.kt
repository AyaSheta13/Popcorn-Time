package com.example.popcorntime.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import com.example.popcorntime.R
import com.example.popcorntime.data.database.PlaylistManager
import com.example.popcorntime.theme.LocalThemeManager
import com.example.popcorntime.theme.ThemeManager
import com.example.popcorntime.ui.theme.orbitronsFontFamily

class MainScreen(private val userName: String = "User") : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeManager = LocalThemeManager.current
        val isDarkTheme by themeManager.isDarkTheme
        val colors = themeManager.currentColors

        val systemUiController = rememberSystemUiController()

        SideEffect {
            systemUiController.setStatusBarColor(colors.background, darkIcons = !isDarkTheme)
            systemUiController.setNavigationBarColor(colors.background, darkIcons = !isDarkTheme)
        }

        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedIndex by remember { mutableIntStateOf(0) }
        var showThemeDialog by remember { mutableStateOf(false) }

        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val drawerWidth = screenWidth * 0.75f

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                // استبدل ModalDrawerSheet بـ Surface مخصص
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(drawerWidth),
                    color = colors.surface, // استخدام لون السطح من السمة المخصصة
                    shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp, bottom = 32.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.my_icon),
                                contentDescription = "App Logo",
                                modifier = Modifier
                                    .size(60.dp)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(12.dp),
                                        clip = false
                                    )
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Popcorn Time",
                                    fontFamily = orbitronsFontFamily,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 18.sp,
                                    color = colors.onSurface, // استخدام onSurface بدلاً من onBackground
                                )
                                Text(
                                    text = "Welcome, $userName",
                                    fontSize = 14.sp,
                                    fontFamily = orbitronsFontFamily,
                                    fontWeight = FontWeight.Light,
                                    color = colors.secondary
                                )
                            }
                        }

                        val menuItems = listOf(
                            MenuItem("Home", Icons.Filled.Home),
                            MenuItem("Theme", Icons.Filled.Palette),
                            MenuItem("Language", Icons.Filled.Language),
                            MenuItem("Help & Support", Icons.Filled.Help),
                            MenuItem("Logout", Icons.Filled.ExitToApp)
                        )

                        menuItems.forEachIndexed { index, item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        scope.launch { drawerState.close() }
                                        when (index) {
                                            0 -> selectedIndex = 0
                                            1 -> {
                                                showThemeDialog = true
                                            }
                                            4 -> navigator.pop()
                                        }
                                    }
                                    .padding(vertical = 12.dp, horizontal = 8.dp)
                            ) {
                                Icon(
                                    item.icon,
                                    contentDescription = item.title,
                                    tint = if (index == 0) colors.primary else colors.onSurface // استخدام onSurface
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    item.title,
                                    color = if (index == 0) colors.primary else colors.onSurface, // استخدام onSurface
                                    fontSize = 16.sp,
                                    fontFamily = orbitronsFontFamily,
                                    fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            if (index == 2) {
                                Divider(
                                    color = colors.secondary.copy(alpha = 0.3f), // استخدام لون ثانوي مع شفافية
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            },
            content = {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = when (selectedIndex) {
                                        0 -> "Home"
                                        1 -> "Favorites"
                                        2 -> "My Lists"
                                        3 -> "Profile"
                                        else -> "Popcorn Time"
                                    },
                                    fontFamily = orbitronsFontFamily,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 20.sp,
                                    color = colors.onBackground,
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Menu",
                                        tint = colors.onBackground
                                    )
                                }
                            },
                            actions = {
                                if (selectedIndex == 0) {
                                    IconButton(onClick = {
                                        navigator.push(SearchScreen())
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = "Search",
                                            tint = colors.onBackground
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = colors.surface
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = colors.surface
                        ) {
                            val items = listOf(
                                Triple("Home", Icons.Filled.Home, Icons.Outlined.Home),
                                Triple(
                                    "Favorites",
                                    Icons.Filled.Favorite,
                                    Icons.Outlined.FavoriteBorder
                                ),
                                Triple("My Lists", Icons.Filled.List, Icons.Outlined.List),
                                Triple("Profile", Icons.Filled.Person, Icons.Outlined.Person)
                            )

                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        val icon =
                                            if (selectedIndex == index) item.third else item.second
                                        Icon(
                                            icon,
                                            contentDescription = item.first,
                                            tint = if (selectedIndex == index) colors.primary else colors.onSurface // استخدام onSurface
                                        )
                                    },
                                    label = {
                                        Text(
                                            item.first,
                                            fontFamily = orbitronsFontFamily,
                                            fontWeight = FontWeight.Light,
                                            color = if (selectedIndex == index) colors.primary else colors.onSurface // استخدام onSurface
                                        )
                                    },
                                    selected = selectedIndex == index,
                                    onClick = { selectedIndex = index },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = Color.Transparent,
                                        selectedIconColor = colors.primary,
                                        selectedTextColor = colors.primary,
                                        unselectedIconColor = colors.onSurface, // استخدام onSurface
                                        unselectedTextColor = colors.onSurface // استخدام onSurface
                                    )
                                )
                            }
                        }
                    },
                    floatingActionButton = {
                        if (selectedIndex == 2) {
                            FloatingActionButton(
                                onClick = {
                                    PlaylistManager.showAddPlaylistDialog = true
                                    PlaylistManager.editingPlaylist = null
                                },
                                containerColor = colors.primary,
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add New List",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    containerColor = colors.background
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(colors.background)
                    ) {
                        when (selectedIndex) {
                            0 -> MoviesContent()
                            1 -> FavoritesContent()
                            2 -> PlaylistsContent()
                            3 -> ProfileContent(userName)
                        }
                    }
                }
            }
        )

        if (showThemeDialog) {
            ThemeSelectionDialog(
                onDismiss = { showThemeDialog = false }
            )
        }
    }
}

data class MenuItem(val title: String, val icon: ImageVector)