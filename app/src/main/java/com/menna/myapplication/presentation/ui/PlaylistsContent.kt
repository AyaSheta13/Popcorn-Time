package com.example.popcorntime.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.popcorntime.R
import com.example.popcorntime.data.database.PlaylistManager
import com.example.popcorntime.data.model.Movie
import com.example.popcorntime.data.model.Playlist
import com.example.popcorntime.theme.ThemeManager
import com.example.popcorntime.ui.theme.orbitronsFontFamily

@Composable
fun PlaylistsContent() {
    val playlists = PlaylistManager.playlists
    val navigator = LocalNavigator.currentOrThrow
    val colors = ThemeManager.currentColors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            if (playlists.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.List,
                                contentDescription = "No Lists",
                                tint = colors.primary,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No Playlists Yet",
                                color = colors.onBackground,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = orbitronsFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Create your first playlist to organize your movies",
                                color = colors.secondary,
                                fontSize = 16.sp,
                                fontFamily = orbitronsFontFamily,
                                fontWeight = FontWeight.Light,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(playlists) { playlist ->
                    PlaylistItem(playlist = playlist)
                }
            }
        }

        if (PlaylistManager.showAddPlaylistDialog) {
            AddEditPlaylistDialog()
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist) {
    val navigator = LocalNavigator.currentOrThrow
    var showOptions by remember { mutableStateOf(false) }
    val colors = ThemeManager.currentColors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                navigator.push(PlaylistDetailsScreen(playlist))
            }
            .background(colors.cardBackground)
    ) {
        Image(
            painter = painterResource(id = R.drawable.playlist_background_image),
            contentDescription = playlist.name,
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
                        startY = 100f
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { showOptions = true },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0x80000000), CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Options",
                    tint = Color.White
                )
            }

            if (showOptions) {
                PlaylistOptionsMenu(
                    playlist = playlist,
                    onDismiss = { showOptions = false }
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Text(
                text = playlist.name,
                fontSize = 24.sp,
                fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Bold,
                color = colors.onCard,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${playlist.movieCount} movies",
                fontSize = 14.sp,
                fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Light,
                color = colors.secondary
            )
        }
    }
}

@Composable
fun PlaylistOptionsMenu(playlist: Playlist, onDismiss: () -> Unit) {
    val colors = ThemeManager.currentColors

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .width(280.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = colors.surface
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            PlaylistManager.editPlaylist(playlist)
                            onDismiss()
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Rename",
                        tint = colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Rename",
                        color = colors.onBackground,
                        fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            PlaylistManager.deletePlaylist(playlist)
                            onDismiss()
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Delete",
                        color = Color.Red,
                        fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}

@Composable
fun AddEditPlaylistDialog(
    onDismiss: (() -> Unit)? = null
) {
    var playlistName by remember {
        mutableStateOf(PlaylistManager.editingPlaylist?.name ?: "")
    }
    var playlistDescription by remember {
        mutableStateOf(PlaylistManager.editingPlaylist?.description ?: "")
    }
    val isEditing = PlaylistManager.editingPlaylist != null
    val colors = ThemeManager.currentColors

    Dialog(onDismissRequest = {
        PlaylistManager.showAddPlaylistDialog = false
        PlaylistManager.editingPlaylist = null
        onDismiss?.invoke()
    }) {
        Surface(
            modifier = Modifier
                .width(320.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = colors.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = if (isEditing) "Edit Playlist" else "Create New Playlist",
                    fontSize = 20.sp,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.background)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.playlist_background_image),
                        contentDescription = "Playlist Cover",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = if (playlistName.isNotBlank()) playlistName else "Playlist Name",
                        color = Color.White,
                        fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .background(Color(0x80000000))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = playlistName,
                    onValueChange = { playlistName = it },
                    label = {
                        Text("Playlist Name",
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light,
                            color = colors.secondary)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = colors.onBackground,
                        unfocusedTextColor = colors.onBackground,
                        cursorColor = colors.onBackground,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = colors.primary,
                        unfocusedIndicatorColor = colors.secondary,
                        focusedLabelColor = colors.primary,
                        unfocusedLabelColor = colors.secondary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = playlistDescription,
                    onValueChange = { playlistDescription = it },
                    label = {
                        Text("Description (Optional)",
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light,
                            color = colors.secondary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = colors.onBackground,
                        unfocusedTextColor = colors.onBackground,
                        cursorColor = colors.onBackground,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = colors.primary,
                        unfocusedIndicatorColor = colors.secondary,
                        focusedLabelColor = colors.primary,
                        unfocusedLabelColor = colors.secondary
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            PlaylistManager.showAddPlaylistDialog = false
                            PlaylistManager.editingPlaylist = null
                            onDismiss?.invoke()
                        }
                    ) {
                        Text(
                            "Cancel",
                            color = colors.secondary,
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            if (playlistName.isNotBlank()) {
                                if (isEditing) {
                                    val updatedPlaylist = PlaylistManager.editingPlaylist!!.copy(
                                        name = playlistName,
                                        description = playlistDescription,
                                        coverImageRes = R.drawable.playlist_background_image
                                    )
                                    PlaylistManager.updatePlaylist(updatedPlaylist)
                                } else {
                                    val newPlaylist = Playlist(
                                        id = 0,
                                        name = playlistName,
                                        description = playlistDescription,
                                        coverImageRes = R.drawable.playlist_background_image
                                    )
                                    PlaylistManager.addPlaylist(newPlaylist)
                                }
                                PlaylistManager.showAddPlaylistDialog = false
                                PlaylistManager.editingPlaylist = null
                                onDismiss?.invoke()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                    ) {
                        Text(
                            if (isEditing) "Update" else "Create",
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
fun PlaylistSelectionItem(playlist: Playlist, movie: Movie, onAddToPlaylist: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAddToPlaylist() }
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E131F)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = playlist.coverImageRes ?: R.drawable.playlist_background_image),
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
                    color = Color.White,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    "${playlist.movieCount} movies",
                    color = Color(0xFFA0A0A0),
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
            }
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add to playlist",
                tint = Color(0xFF9B27B0),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}