package com.example.popcorntime.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.popcorntime.data.database.PlaylistManager
import com.example.popcorntime.data.model.Movie
import com.example.popcorntime.data.model.Playlist
import com.example.popcorntime.theme.ThemeManager
import com.example.popcorntime.ui.theme.orbitronsFontFamily

class PlaylistDetailsScreen(private val playlist: Playlist) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val colors = ThemeManager.currentColors

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = playlist.name,
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            color = colors.onBackground,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = colors.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = colors.surface
                    )
                )
            },
            containerColor = colors.background
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(colors.background)
            ) {
                PlaylistDetailsContent(playlist = playlist)
            }
        }
    }
}

@Composable
fun PlaylistDetailsContent(playlist: Playlist) {
    val movies = playlist.movies
    val navigator = LocalNavigator.currentOrThrow
    val colors = ThemeManager.currentColors

    if (movies.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    Icons.Filled.Movie,
                    contentDescription = "No Movies",
                    tint = colors.primary,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "No Movies Yet",
                    color = colors.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = orbitronsFontFamily,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Add movies to this playlist to see them here",
                    color = colors.secondary,
                    fontSize = 16.sp,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        navigator.pop()
                        navigateToMoviesTab(navigator)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                ) {
                    Text(
                        "Browse Movies",
                        fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(movies) { movie ->
                PlaylistMovieItem(movie = movie, playlistId = playlist.id)
            }
        }
    }
}

fun navigateToMoviesTab(navigator: cafe.adriel.voyager.navigator.Navigator) {
    navigator.popUntil { it is MainScreen }
}

@Composable
fun PlaylistMovieItem(movie: Movie, playlistId: Int) {
    val navigator = LocalNavigator.currentOrThrow
    var showOptions by remember { mutableStateOf(false) }
    val colors = ThemeManager.currentColors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigator.push(MovieDetailsScreen(movie))
            }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = movie.imageRes),
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    IconButton(
                        onClick = { showOptions = true },
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0x80000000), CircleShape)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Options",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colors.onBackground,
                fontFamily = orbitronsFontFamily,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = Color.Yellow,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format("%.1f", movie.rating),
                    fontSize = 12.sp,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light,
                    color = colors.onBackground
                )
            }
        }

        if (showOptions) {
            DropdownMenu(
                expanded = showOptions,
                onDismissRequest = { showOptions = false },
                modifier = Modifier.background(colors.surface)
            ) {
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Remove from Playlist",
                                color = Color.Red,
                                fontFamily = orbitronsFontFamily,
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp
                            )
                        }
                    },
                    onClick = {
                        PlaylistManager.removeMovieFromPlaylist(movie.id, playlistId)
                        showOptions = false
                    }
                )
            }
        }
    }
}