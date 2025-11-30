package com.popcorntime.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.popcorntime.data.database.FavoritesManager
import com.example.popcorntime.data.model.Movie
import com.example.popcorntime.presentation.ui.MovieDetailsScreen
import com.example.popcorntime.theme.ThemeManager
import com.example.popcorntime.ui.theme.orbitronsFontFamily

class FavoritesScreen : Screen {
    @Composable
    override fun Content() {
        FavoritesContent()
    }
}

@Composable
fun FavoritesContent() {
    val favoriteMovies = FavoritesManager.favoriteMovies
    val navigator = LocalNavigator.currentOrThrow
    val colors = ThemeManager.currentColors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        if (favoriteMovies.isEmpty()) {
            EmptyFavoritesState()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favoriteMovies, key = { it.id }) { movie ->
                    FavoriteMovieGridItem(
                        movie = movie,
                        onMovieClick = {
                            navigator.push(MovieDetailsScreen(movie))
                        },
                        onRemoveFavorite = { FavoritesManager.removeFromFavorites(movie.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteMovieGridItem(
    movie: Movie,
    onMovieClick: () -> Unit,
    onRemoveFavorite: () -> Unit
) {
    val colors = ThemeManager.currentColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMovieClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
        ) {
            Image(
                painter = painterResource(id = movie.imageRes),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                colors.background
                            ),
                            startY = 300f
                        )
                    )
            )

            IconButton(
                onClick = { onRemoveFavorite() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Remove from favorites",
                    tint = colors.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(colors.cardBackground, RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = String.format("%.1f", movie.rating),
                        fontSize = 12.sp,
                        fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light,
                        color = colors.onCard,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = movie.title,
            fontSize = 14.sp,
            color = colors.onCard,
            fontFamily = orbitronsFontFamily,
            fontWeight = FontWeight.Light,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = movie.category,
            fontSize = 12.sp,
            fontFamily = orbitronsFontFamily,
            fontWeight = FontWeight.Light,
            color = colors.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EmptyFavoritesState() {
    val colors = ThemeManager.currentColors

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Favorite,
                contentDescription = "Favorites",
                tint = colors.primary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Your Favorite Movies",
                color = colors.onBackground,
                fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 24.sp,
            )
            Text(
                "Movies you love will appear here",
                color = colors.secondary,
                fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun MovieOptionsMenu(movie: Movie) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val isFavorite = FavoritesManager.isFavorite(movie.id)
    val colors = ThemeManager.currentColors

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "More options",
                tint = colors.onBackground
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(colors.surface)
        ) {
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = if (isFavorite) colors.primary else colors.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                            color = colors.onBackground,
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    }
                },
                onClick = {
                    FavoritesManager.toggleFavorite(movie)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.List,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Add to List",
                            color = colors.onBackground,
                            fontSize = 14.sp,
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light)
                    }
                },
                onClick = {
                    expanded = false
                    showDialog = true
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text("Add to List",
                        color = colors.onBackground,
                        fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light)
                },
                text = {
                    Text("Select a list to add this movie to:",
                        color = colors.onBackground)
                },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Add",
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light,
                            color = colors.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel",
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light,
                            color = colors.secondary)
                    }
                },
                containerColor = colors.surface
            )
        }
    }
}