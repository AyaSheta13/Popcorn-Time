package com.example.popcorntime.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.popcorntime.data.model.Category
import com.example.popcorntime.data.model.Movie
import com.example.popcorntime.theme.ThemeManager
import com.example.popcorntime.ui.theme.orbitronsFontFamily

data class SeeAllScreen(val category: Category) : Screen {
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
                            text = category.name,
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
            SeeAllContent(
                category = category,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(colors.background)
            )
        }
    }
}

@Composable
fun SeeAllContent(
    category: Category,
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val movies = category.movies
    val colors = ThemeManager.currentColors

    Column(modifier = modifier) {
        // عدد الأفلام
        Text(
            text = "${movies.size} Movies",
            color = colors.secondary,
            fontFamily = orbitronsFontFamily,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // شبكة الأفلام
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(movies, key = { it.id }) { movie ->
                SeeAllMovieItem(
                    movie = movie,
                    onMovieClick = {
                        navigator.push(MovieDetailsScreen(movie))
                    }
                )
            }
        }
    }
}

@Composable
fun SeeAllMovieItem(
    movie: Movie,
    onMovieClick: () -> Unit
) {
    val colors = ThemeManager.currentColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMovieClick() }
    ) {
        // صورة الفيلم
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

            // تدرج لوني للأسفل
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                colors.background
                            ),
                            startY = 300f
                        )
                    )
            )

            // التقييم في الزاوية
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xCC000000), RoundedCornerShape(8.dp))
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
                        color = Color.White,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // اسم الفيلم
        Text(
            text = movie.title,
            fontSize = 14.sp,
            color = colors.onCard,
            fontFamily = orbitronsFontFamily,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        // التصنيف
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