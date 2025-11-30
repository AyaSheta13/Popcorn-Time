package com.example.popcorntime.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.popcorntime.R
import com.example.popcorntime.data.model.Category
import com.example.popcorntime.data.model.Movie
import com.example.popcorntime.theme.ThemeManager
import com.example.popcorntime.ui.theme.orbitronsFontFamily

@Composable
fun MoviesContent() {
    val colors = ThemeManager.currentColors

    val featuredMovies = listOf(
        Movie(1, "Avengers: Endgame", 4.8, R.drawable.no_internet_image, "Action"),
        Movie(2, "The Dark Knight", 4.9, R.drawable.no_internet_image, "Action"),
        Movie(3, "Inception", 4.7, R.drawable.no_internet_image, "Sci-Fi"),
        Movie(4, "Interstellar", 4.8, R.drawable.no_internet_image, "Sci-Fi"),
        Movie(5, "The Shawshank Redemption", 4.9, R.drawable.no_internet_image, "Drama")
    )

    val categories = listOf(
        Category(1, "Action", featuredMovies.filter { it.category == "Action" }),
        Category(2, "Sci-Fi", featuredMovies.filter { it.category == "Sci-Fi" }),
        Category(3, "Drama", featuredMovies.filter { it.category == "Drama" }),
        Category(4, "Comedy", featuredMovies),
        Category(5, "Horror", featuredMovies),
        Category(6, "Romance", featuredMovies)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            FeaturedMoviesSection(featuredMovies)
        }

        items(categories) { category ->
            CategorySection(category = category)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedMoviesSection(movies: List<Movie>) {
    val colors = ThemeManager.currentColors
    val pagerState = rememberPagerState(pageCount = { movies.size })

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        ) { page ->
            val movie = movies[page]
            FeaturedMovieItem(movie = movie)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(movies.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) colors.primary else colors.secondary
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun FeaturedMovieItem(movie: Movie) {
    val navigator = LocalNavigator.currentOrThrow
    val colors = ThemeManager.currentColors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(400.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(colors.cardBackground)
            .clickable {
                navigator.push(MovieDetailsScreen(movie))
            }
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

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Text(
                text = movie.title,
                fontSize = 32.sp,
                fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Bold,
                color = colors.onCard,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            MovieRating(rating = movie.rating)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* حدث تشغيل التريلر */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Watch Trailer",
                        color = Color.White,
                        fontFamily = orbitronsFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySection(category: Category) {
    val navigator = LocalNavigator.currentOrThrow
    val colors = ThemeManager.currentColors

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name,
                fontSize = 22.sp,
                fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Row(
                modifier = Modifier
                    .clickable {
                        navigator.push(SeeAllScreen(category))
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View All",
                    fontSize = 14.sp,
                    color = colors.primary,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "View All",
                    tint = colors.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(category.movies) { movie ->
                MovieGridItem(movie = movie)
            }
        }
    }
}

@Composable
fun MovieGridItem(movie: Movie) {
    val navigator = LocalNavigator.currentOrThrow
    val colors = ThemeManager.currentColors

    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable { navigator.push(MovieDetailsScreen(movie)) }
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
            color = colors.onCard,
            maxLines = 2,
            fontFamily = orbitronsFontFamily,
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
                color = colors.onCard
            )
        }
    }
}
@Composable
fun MovieRating(rating: Double) {
    val colors = ThemeManager.currentColors

    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            val starColor = if (index < rating.toInt()) Color.Yellow else colors.secondary
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Star",
                tint = starColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${rating}/5",
            fontSize = 16.sp,
            fontFamily = orbitronsFontFamily,
            color = colors.onCard,
            fontWeight = FontWeight.Medium
        )
    }
}