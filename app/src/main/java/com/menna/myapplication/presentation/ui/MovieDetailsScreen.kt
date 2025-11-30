package com.example.popcorntime.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.popcorntime.R
import com.example.popcorntime.data.database.FavoritesManager
import com.example.popcorntime.data.database.PlaylistManager
import com.example.popcorntime.data.model.CastMember
import com.example.popcorntime.data.model.Movie
import com.example.popcorntime.data.model.Playlist
import com.example.popcorntime.theme.ThemeManager
import com.example.popcorntime.ui.theme.orbitronsFontFamily

data class MovieDetailsScreen(val movie: Movie) : Screen {
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
                            text = "Movie Details",
                            fontSize = 20.sp,
                            color = colors.onBackground,
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.SemiBold
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
                    actions = {
                        MovieOptionsMenuMovieDetails(movie = movie)
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = colors.surface
                    )
                )
            },
            containerColor = colors.background
        ) { innerPadding ->
            MovieDetailsContent(
                movie = movie,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(colors.background)
            )
        }
    }
}

@Composable
fun MovieOptionsMenuMovieDetails(movie: Movie) {
    var expanded by remember { mutableStateOf(false) }
    var showPlaylistDialog by remember { mutableStateOf(false) }
    val isFavorite = FavoritesManager.isFavorite(movie.id)
    val playlists = PlaylistManager.playlists
    val navigator = LocalNavigator.currentOrThrow
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
                        Icon(Icons.Filled.List,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Add to Playlist",
                            color = colors.onBackground,
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp)
                    }
                },
                onClick = {
                    expanded = false
                    showPlaylistDialog = true
                }
            )
        }

        if (showPlaylistDialog) {
            PlaylistSelectionDialog(
                movie = movie,
                onDismiss = { showPlaylistDialog = false },
                onCreateNewPlaylist = {
                    showPlaylistDialog = false
                    PlaylistManager.showAddPlaylistDialog = true
                    PlaylistManager.editingPlaylist = null
                },
                onNavigateToMovies = {
                    navigator.pop()
                }
            )
        }

        if (PlaylistManager.showAddPlaylistDialog) {
            AddEditPlaylistDialog(
                onDismiss = {
                    PlaylistManager.showAddPlaylistDialog = false
                    PlaylistManager.editingPlaylist = null
                    if (PlaylistManager.playlists.isNotEmpty()) {
                        showPlaylistDialog = true
                    }
                }
            )
        }
    }
}

@Composable
fun PlaylistSelectionDialog(
    movie: Movie,
    onDismiss: () -> Unit,
    onCreateNewPlaylist: () -> Unit,
    onNavigateToMovies: () -> Unit
) {
    val playlists = PlaylistManager.playlists
    val colors = ThemeManager.currentColors
    val isMovieInPlaylist = remember(playlists, movie) {
        playlists.associate { playlist ->
            playlist.id to playlist.movies.any { it.id == movie.id }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add to Playlist",
                fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Light,
                color = colors.onBackground)
        },
        text = {
            Column {
                if (playlists.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                    ) {
                        Icon(
                            Icons.Filled.List,
                            contentDescription = "No Playlists",
                            tint = colors.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No playlists available",
                            color = colors.onBackground,
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Create your first playlist to organize movies",
                            color = colors.secondary,
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    Text(
                        "Select a playlist:",
                        fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light,
                        color = colors.onBackground,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.height(200.dp)
                    ) {
                        items(playlists) { playlist ->
                            val isInPlaylist = isMovieInPlaylist[playlist.id] ?: false
                            PlaylistSelectionItem(
                                playlist = playlist,
                                movie = movie,
                                isInPlaylist = isInPlaylist,
                                onAddToPlaylist = {
                                    if (isInPlaylist) {
                                        PlaylistManager.removeMovieFromPlaylist(movie.id, playlist.id)
                                    } else {
                                        PlaylistManager.addMovieToPlaylist(movie, playlist.id)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onCreateNewPlaylist) {
                    Text("Create New",
                        fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light,
                        color = colors.primary)
                }

                Row {
                    if (playlists.isEmpty()) {
                        TextButton(onClick = onNavigateToMovies) {
                            Text("Browse Movies",
                                fontFamily = orbitronsFontFamily,
                                fontWeight = FontWeight.Light,
                                color = colors.primary)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    TextButton(onClick = onDismiss) {
                        Text("Cancel",
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light,
                            color = colors.secondary)
                    }
                }
            }
        },
        containerColor = colors.surface
    )
}

@Composable
fun PlaylistSelectionItem(
    playlist: Playlist,
    movie: Movie,
    isInPlaylist: Boolean,
    onAddToPlaylist: () -> Unit
) {
    val colors = ThemeManager.currentColors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAddToPlaylist() }
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isInPlaylist) colors.primary.copy(alpha = 0.2f) else colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.playlist_background_image),
                contentDescription = playlist.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    playlist.name,
                    color = colors.onBackground,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    "${playlist.movieCount} movies",
                    color = colors.secondary,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
            }

            if (isInPlaylist) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Remove from playlist",
                    tint = colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add to playlist",
                    tint = colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
@Composable
fun MovieDetailsContent(movie: Movie, modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.currentOrThrow
    val colors = ThemeManager.currentColors

    LazyColumn(
        modifier = modifier
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            ) {
                Image(
                    painter = painterResource(id = movie.imageRes),
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    colors.background
                                ),
                                startY = 300f
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp)
                ) {
                    FloatingActionButton(
                        onClick = { /* Play trailer action */ },
                        containerColor = colors.primary,
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Watch Trailer",
                            tint = Color.White
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = movie.title,
                    fontSize = 32.sp,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MovieRatingBar(rating = movie.rating)

                    Text(
                        text = "2023",
                        fontSize = 16.sp,
                        fontFamily = orbitronsFontFamily,
                        color = colors.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val categories = listOf("Action", "Adventure", "Sci-Fi")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(category = category)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                StorylineSection()

                Spacer(modifier = Modifier.height(32.dp))

                CastSection()

                Spacer(modifier = Modifier.height(32.dp))

                SimilarMoviesSection(navigator = navigator)
            }
        }
    }
}

@Composable
fun MovieRatingBar(rating: Double) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "Rating",
            tint = Color.Yellow,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = String.format("%.1f", rating),
            fontSize = 18.sp,
            fontFamily = orbitronsFontFamily,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CategoryChip(category: String) {
    val colors = ThemeManager.currentColors

    Surface(
        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = colors.primary
        )
    ) {
        Text(
            text = category,
            color = colors.onBackground,
            fontSize = 12.sp,
            fontFamily = orbitronsFontFamily,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun StorylineSection() {
    var expanded by remember { mutableStateOf(false) }
    val colors = ThemeManager.currentColors
    val storyline = "When the Avengers become separated and defeated by a new threat, " +
            "they must reunite and work together with their allies to restore balance " +
            "to the universe and protect humanity from total destruction. " +
            "This epic conclusion to the saga brings together all your favorite heroes " +
            "for one final battle that will determine the fate of the entire universe."

    Column {
        Text(
            text = "Storyline",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground,
            fontFamily = orbitronsFontFamily,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = if (expanded) storyline else storyline.take(150) + "...",
            fontSize = 14.sp,
            color = colors.secondary,
            fontFamily = orbitronsFontFamily,
            fontWeight = FontWeight.Light,
            lineHeight = 20.sp
        )

        if (!expanded) {
            Text(
                text = "Read More",
                fontSize = 14.sp,
                fontFamily = orbitronsFontFamily,
                color = colors.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun CastSection() {
    val castMembers = listOf(
        CastMember("Robert Downey Jr.", R.drawable.no_found_cast_image),
        CastMember("Chris Evans", R.drawable.no_found_cast_image),
        CastMember("Scarlett Johansson", R.drawable.no_found_cast_image),
        CastMember("Mark Ruffalo", R.drawable.no_found_cast_image),
        CastMember("Chris Hemsworth", R.drawable.no_found_cast_image)
    )

    Column {
        Text(
            text = "Cast",
            fontSize = 20.sp,
            fontFamily = orbitronsFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(castMembers) { castMember ->
                CastMemberItem(castMember = castMember)
            }
        }
    }
}

@Composable
fun CastMemberItem(castMember: CastMember) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Image(
            painter = painterResource(id = castMember.imageRes),
            contentDescription = castMember.name,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = castMember.name,
            fontSize = 12.sp,
            fontFamily = orbitronsFontFamily,
            fontWeight = FontWeight.Light,
            color = Color.White,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SimilarMoviesSection(navigator: cafe.adriel.voyager.navigator.Navigator) {
    val colors = ThemeManager.currentColors
    val similarMovies = listOf(
        Movie(6, "Avengers: Infinity War", 4.7, R.drawable.no_internet_image, "Action"),
        Movie(7, "Guardians of the Galaxy", 4.6, R.drawable.no_internet_image, "Action"),
        Movie(8, "Doctor Strange", 4.5, R.drawable.no_internet_image, "Action"),
        Movie(9, "Black Panther", 4.8, R.drawable.no_internet_image, "Action"),
        Movie(10, "Captain Marvel", 4.4, R.drawable.no_internet_image, "Action")
    )

    Column {
        Text(
            text = "Similar Movies",
            fontSize = 20.sp,
            fontFamily = orbitronsFontFamily,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(similarMovies) { movie ->
                SimilarMovieItem(movie = movie, navigator = navigator)
            }
        }
    }
}

@Composable
fun SimilarMovieItem(movie: Movie, navigator: cafe.adriel.voyager.navigator.Navigator) {
    val colors = ThemeManager.currentColors

    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable {
                navigator.push(MovieDetailsScreen(movie))
            }
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(id = movie.imageRes),
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
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
}