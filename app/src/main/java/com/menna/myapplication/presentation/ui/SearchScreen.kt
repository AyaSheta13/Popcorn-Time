package com.example.popcorntime.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.popcorntime.R
import com.example.popcorntime.data.model.Movie
import com.example.popcorntime.theme.ThemeManager
import com.example.popcorntime.ui.theme.orbitronsFontFamily
import kotlin.math.max

class SearchScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val colors = ThemeManager.currentColors

        Scaffold(
            topBar = {
                SearchTopBar(navigator = navigator)
            },
            containerColor = colors.background
        ) { innerPadding ->
            SearchContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(colors.background)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(navigator: cafe.adriel.voyager.navigator.Navigator) {
    var showFilterDialog by remember { mutableStateOf(false) }
    val colors = ThemeManager.currentColors

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Search",
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
        actions = {
            IconButton(onClick = { showFilterDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = "Filter",
                    tint = colors.onBackground
                )
            }

            if (showFilterDialog) {
                AdvancedFilterDialog(
                    currentFilters = FilterOptions(),
                    onDismiss = { showFilterDialog = false },
                    onApplyFilters = { filters ->
                        // تطبيق الفلاتر هنا
                        showFilterDialog = false
                    }
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colors.surface
        )
    )
}

@Composable
fun SearchContent(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }
    var activeFilters by remember { mutableStateOf(FilterOptions()) }
    val focusManager = LocalFocusManager.current
    val colors = ThemeManager.currentColors

    // بيانات تجريبية للبحث
    val allMovies = listOf(
        Movie(1, "Avengers: Endgame", 4.8, R.drawable.no_internet_image, "Action"),
        Movie(2, "The Dark Knight", 4.9, R.drawable.no_internet_image, "Action"),
        Movie(3, "Inception", 4.7, R.drawable.no_internet_image, "Sci-Fi"),
        Movie(4, "Interstellar", 4.8, R.drawable.no_internet_image, "Sci-Fi"),
        Movie(5, "The Shawshank Redemption", 4.9, R.drawable.no_internet_image, "Drama"),
        Movie(6, "The Godfather", 4.9, R.drawable.no_internet_image, "Crime"),
        Movie(7, "Pulp Fiction", 4.8, R.drawable.no_internet_image, "Crime"),
        Movie(8, "Forrest Gump", 4.8, R.drawable.no_internet_image, "Drama"),
        Movie(9, "The Matrix", 4.7, R.drawable.no_internet_image, "Sci-Fi"),
        Movie(10, "Spider-Man: No Way Home", 4.6, R.drawable.no_internet_image, "Action"),
        Movie(11, "The Batman", 4.5, R.drawable.no_internet_image, "Action"),
        Movie(12, "Dune", 4.4, R.drawable.no_internet_image, "Sci-Fi")
    )

    // فلترة الأفلام بناءً على البحث والفلاتر
    val searchResults = remember(searchQuery, activeFilters) {
        var filteredMovies = allMovies

        // تطبيق البحث النصي
        if (searchQuery.isNotBlank()) {
            filteredMovies = filteredMovies.filter { movie ->
                movie.title.contains(searchQuery, ignoreCase = true) ||
                        movie.category.contains(searchQuery, ignoreCase = true)
            }
        }

        // تطبيق فلاتر التصنيفات
        if (activeFilters.categories.isNotEmpty()) {
            filteredMovies = filteredMovies.filter { movie ->
                activeFilters.categories.any { category ->
                    movie.category.equals(category, ignoreCase = true)
                }
            }
        }

        // تطبيق فلتر التقييم
        if (activeFilters.minRating > 0) {
            filteredMovies = filteredMovies.filter { movie ->
                movie.rating >= activeFilters.minRating
            }
        }

        // تطبيق فلتر السنة
        if (activeFilters.year.isNotBlank()) {
            // في التطبيق الحقيقي، سيكون لديك خاصية السنة في نموذج الفيلم
            // هنا نستخدم محاكاة بسيطة
            filteredMovies = when (activeFilters.year) {
                "2023" -> filteredMovies.take(4)
                "2022" -> filteredMovies.drop(4).take(4)
                else -> filteredMovies
            }
        }

        filteredMovies
    }

    val hasActiveFilters = remember(activeFilters) {
        activeFilters.categories.isNotEmpty() ||
                activeFilters.minRating > 0 ||
                activeFilters.year.isNotBlank() ||
                activeFilters.minDuration > 0 ||
                activeFilters.maxDuration < 300
    }

    Column(modifier = modifier) {
        // شريط البحث
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onClearClick = {
                searchQuery = ""
                activeFilters = FilterOptions() // إعادة تعيين الفلاتر عند المسح
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // قسم الفلاتر السريعة - معدلة لتعرض المحتوى مباشرة
        QuickFiltersSection(
            activeFilters = activeFilters,
            onFilterChange = { newFilters -> activeFilters = newFilters },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // عرض الفلاتر النشطة
        if (hasActiveFilters) {
            ActiveFiltersSection(
                activeFilters = activeFilters,
                onClearFilters = { activeFilters = FilterOptions() },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // نتائج البحث
        if (searchQuery.isNotBlank() || hasActiveFilters) {
            Text(
                text = "Results (${searchResults.size})",
                color = colors.onBackground,
                fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (searchResults.isEmpty()) {
                // حالة عدم وجود نتائج
                NoResultsState(
                    searchQuery = searchQuery,
                    hasActiveFilters = hasActiveFilters,
                    onClearFilters = { activeFilters = FilterOptions() }
                )
            } else {
                // عرض نتائج البحث
                SearchResultsGrid(movies = searchResults)
            }
        } else {
            // حالة البحث الفارغ - عرض زر الفلترة
            EmptySearchState(onFilterClick = { showFilterDialog = true })
        }
    }

    // نافذة الفلترة المتقدمة - تفتح فقط من أيقونة الفلترة في الأعلى أو زر Open Filters
    if (showFilterDialog) {
        AdvancedFilterDialog(
            currentFilters = activeFilters,
            onDismiss = { showFilterDialog = false },
            onApplyFilters = { filters ->
                activeFilters = filters
                showFilterDialog = false
            }
        )
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val colors = ThemeManager.currentColors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(colors.surface)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // أيقونة البحث
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = colors.primary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // حقل البحث
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Search for a movie...",
                        color = colors.secondary,
                        fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colors.onBackground,
                    unfocusedTextColor = colors.onBackground,
                    cursorColor = colors.onBackground,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                    }
                )
            )

            // زر المسح (يظهر فقط عندما يكون هناك نص)
            if (searchQuery.isNotBlank()) {
                IconButton(
                    onClick = onClearClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear",
                        tint = colors.secondary
                    )
                }
            }
        }
    }
}

@Composable
fun QuickFiltersSection(
    activeFilters: FilterOptions,
    onFilterChange: (FilterOptions) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCategoryMenu by remember { mutableStateOf(false) }
    var showYearMenu by remember { mutableStateOf(false) }
    var showRatingMenu by remember { mutableStateOf(false) }
    val colors = ThemeManager.currentColors

    val categories = listOf("Action", "Sci-Fi", "Drama", "Comedy", "Horror", "Romance", "Crime", "Adventure")
    val years = listOf("2023", "2022", "2021", "2020", "2019", "2018")
    val ratings = listOf("4.5+", "4.0+", "3.5+", "3.0+", "Any")

    Column(modifier = modifier) {
        Text(
            text = "Quick Filters",
            color = colors.onBackground,
            fontFamily = orbitronsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // فلتر التصنيف - يعرض القائمة مباشرة
            item {
                Box {
                    FilterChip(
                        selected = activeFilters.categories.isNotEmpty(),
                        onClick = { showCategoryMenu = true },
                        label = {
                            Text(
                                if (activeFilters.categories.isNotEmpty())
                                    "Category: ${activeFilters.categories.first()}"
                                else "Category: All",
                                fontFamily = orbitronsFontFamily,
                                fontWeight = FontWeight.Light
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "Category filter",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )

                    if (showCategoryMenu) {
                        DropdownMenu(
                            expanded = showCategoryMenu,
                            onDismissRequest = { showCategoryMenu = false },
                            modifier = Modifier.background(colors.surface)
                        ) {
                            // خيار "الكل"
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "All Categories",
                                        color = colors.onBackground,
                                        fontFamily = orbitronsFontFamily,
                                        fontWeight = FontWeight.Light
                                    )
                                },
                                onClick = {
                                    onFilterChange(activeFilters.copy(categories = emptySet()))
                                    showCategoryMenu = false
                                }
                            )

                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            category,
                                            color = colors.onBackground,
                                            fontFamily = orbitronsFontFamily,
                                            fontWeight = FontWeight.Light
                                        )
                                    },
                                    onClick = {
                                        onFilterChange(activeFilters.copy(categories = setOf(category)))
                                        showCategoryMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // فلتر السنة - يعرض القائمة مباشرة
            item {
                Box {
                    FilterChip(
                        selected = activeFilters.year.isNotBlank(),
                        onClick = { showYearMenu = true },
                        label = {
                            Text(
                                "Year: ${activeFilters.year.ifBlank { "All" }}",
                                fontFamily = orbitronsFontFamily,
                                fontWeight = FontWeight.Light
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "Year filter",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )

                    if (showYearMenu) {
                        DropdownMenu(
                            expanded = showYearMenu,
                            onDismissRequest = { showYearMenu = false },
                            modifier = Modifier.background(colors.surface)
                        ) {
                            // خيار "الكل"
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "All Years",
                                        color = colors.onBackground,
                                        fontFamily = orbitronsFontFamily,
                                        fontWeight = FontWeight.Light
                                    )
                                },
                                onClick = {
                                    onFilterChange(activeFilters.copy(year = ""))
                                    showYearMenu = false
                                }
                            )

                            years.forEach { year ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            year,
                                            color = colors.onBackground,
                                            fontFamily = orbitronsFontFamily,
                                            fontWeight = FontWeight.Light
                                        )
                                    },
                                    onClick = {
                                        onFilterChange(activeFilters.copy(year = year))
                                        showYearMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // فلتر التقييم - يعرض القائمة مباشرة
            item {
                Box {
                    FilterChip(
                        selected = activeFilters.minRating > 0,
                        onClick = { showRatingMenu = true },
                        label = {
                            Text(
                                "Rating: ${if (activeFilters.minRating > 0) "${activeFilters.minRating}+" else "All"}",
                                fontFamily = orbitronsFontFamily,
                                fontWeight = FontWeight.Light
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "Rating filter",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )

                    if (showRatingMenu) {
                        DropdownMenu(
                            expanded = showRatingMenu,
                            onDismissRequest = { showRatingMenu = false },
                            modifier = Modifier.background(colors.surface)
                        ) {
                            ratings.forEach { rating ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            rating,
                                            color = colors.onBackground,
                                            fontFamily = orbitronsFontFamily,
                                            fontWeight = FontWeight.Light
                                        )
                                    },
                                    onClick = {
                                        val minRating = when (rating) {
                                            "4.5+" -> 4.5
                                            "4.0+" -> 4.0
                                            "3.5+" -> 3.5
                                            "3.0+" -> 3.0
                                            else -> 0.0
                                        }
                                        onFilterChange(activeFilters.copy(minRating = minRating))
                                        showRatingMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveFiltersSection(
    activeFilters: FilterOptions,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = ThemeManager.currentColors

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Active Filters",
                color = colors.onBackground,
                fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            TextButton(onClick = onClearFilters) {
                Text(
                    "Clear All",
                    color = colors.primary,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // عرض التصنيفات النشطة
            if (activeFilters.categories.isNotEmpty()) {
                items(activeFilters.categories.toList()) { category ->
                    ActiveFilterChip(
                        text = "Category: $category",
                        onRemove = {
                            onClearFilters()
                        }
                    )
                }
            }

            // عرض التقييم النشط
            if (activeFilters.minRating > 0) {
                item {
                    ActiveFilterChip(
                        text = "Rating: ${activeFilters.minRating}+",
                        onRemove = {
                            onClearFilters()
                        }
                    )
                }
            }

            // عرض السنة النشطة
            if (activeFilters.year.isNotBlank()) {
                item {
                    ActiveFilterChip(
                        text = "Year: ${activeFilters.year}",
                        onRemove = {
                            onClearFilters()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveFilterChip(text: String, onRemove: () -> Unit) {
    val colors = ThemeManager.currentColors

    Surface(
        color = colors.primary,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(32.dp)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                Icons.Filled.Close,
                contentDescription = "Remove",
                tint = Color.White,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onRemove() }
            )
        }
    }
}

@Composable
fun EmptySearchState(onFilterClick: () -> Unit) {
    val colors = ThemeManager.currentColors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onFilterClick() }
        ) {
            Icon(
                Icons.Filled.FilterAlt,
                contentDescription = "Filter",
                tint = colors.primary,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Search by Filter",
                color = colors.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = orbitronsFontFamily,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Use filters to find movies by category, rating, year, and more",
                color = colors.secondary,
                fontSize = 16.sp,
                fontFamily = orbitronsFontFamily,
                fontWeight = FontWeight.Light,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onFilterClick,
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
            ) {
                Text(
                    "Open Filters",
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
fun NoResultsState(
    searchQuery: String,
    hasActiveFilters: Boolean,
    onClearFilters: () -> Unit
) {
    val colors = ThemeManager.currentColors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.SearchOff,
                contentDescription = "No results",
                tint = colors.primary,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "No Results Found",
                color = colors.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = orbitronsFontFamily,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (searchQuery.isNotBlank()) {
                Text(
                    "No movies found for \"$searchQuery\"",
                    color = colors.secondary,
                    fontSize = 16.sp,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            if (hasActiveFilters) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Try adjusting your filters",
                    color = colors.secondary,
                    fontSize = 16.sp,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onClearFilters,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                ) {
                    Text(
                        "Clear Filters",
                        fontFamily = orbitronsFontFamily,
                        fontWeight = FontWeight.Light
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Try different keywords",
                    color = colors.secondary,
                    fontSize = 14.sp,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Light,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SearchResultsGrid(movies: List<Movie>) {
    val navigator = LocalNavigator.currentOrThrow
    val colors = ThemeManager.currentColors

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies.chunked(2)) { rowMovies ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowMovies.forEach { movie ->
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        SearchMovieItem(
                            movie = movie,
                            onMovieClick = {
                                navigator.push(MovieDetailsScreen(movie))
                            }
                        )
                    }
                }

                // إضافة عنصر فارغ إذا كان عدد الأفلام في الصف فردي
                if (rowMovies.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SearchMovieItem(movie: Movie, onMovieClick: () -> Unit) {
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
                .clip(RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(id = movie.imageRes),
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
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

@Composable
fun AdvancedFilterDialog(
    currentFilters: FilterOptions,
    onDismiss: () -> Unit,
    onApplyFilters: (FilterOptions) -> Unit
) {
    var selectedCategories by remember { mutableStateOf(currentFilters.categories) }
    var minRating by remember { mutableStateOf(currentFilters.minRating.toFloat()) }
    var selectedYear by remember { mutableStateOf(currentFilters.year) }
    var minDuration by remember { mutableStateOf(currentFilters.minDuration) }
    var maxDuration by remember { mutableStateOf(currentFilters.maxDuration) }
    val colors = ThemeManager.currentColors

    val categories = listOf("Action", "Sci-Fi", "Drama", "Comedy", "Horror", "Romance", "Crime", "Adventure", "Thriller", "Animation")
    val years = listOf("2023", "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp),
            color = colors.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Advanced Filters",
                    fontSize = 24.sp,
                    fontFamily = orbitronsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // تصنيفات الأفلام
                    item {
                        Column {
                            Text(
                                "Categories",
                                color = colors.onBackground,
                                fontFamily = orbitronsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                categories.forEach { category ->
                                    FilterChip(
                                        selected = selectedCategories.contains(category),
                                        onClick = {
                                            selectedCategories = if (selectedCategories.contains(category)) {
                                                selectedCategories - category
                                            } else {
                                                selectedCategories + category
                                            }
                                        },
                                        label = {
                                            Text(category,
                                                fontFamily = orbitronsFontFamily,
                                                fontWeight = FontWeight.Light)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // التقييم الأدنى
                    item {
                        Column {
                            Text(
                                "Minimum Rating: ${String.format("%.1f", minRating)}",
                                color = colors.onBackground,
                                fontFamily = orbitronsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Slider(
                                value = minRating,
                                onValueChange = { minRating = it },
                                valueRange = 0f..5f,
                                steps = 9,
                                modifier = Modifier.fillMaxWidth(),
                                colors = SliderDefaults.colors(
                                    thumbColor = colors.primary,
                                    activeTrackColor = colors.primary
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("0", color = colors.secondary, fontFamily = orbitronsFontFamily)
                                Text("5", color = colors.secondary, fontFamily = orbitronsFontFamily)
                            }
                        }
                    }

                    // سنة الإصدار
                    item {
                        Column {
                            Text(
                                "Release Year",
                                color = colors.onBackground,
                                fontFamily = orbitronsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                item {
                                    FilterChip(
                                        selected = selectedYear.isEmpty(),
                                        onClick = { selectedYear = "" },
                                        label = {
                                            Text("Any Year",
                                                fontFamily = orbitronsFontFamily,
                                                fontWeight = FontWeight.Light)
                                        }
                                    )
                                }
                                items(years) { year ->
                                    FilterChip(
                                        selected = selectedYear == year,
                                        onClick = { selectedYear = year },
                                        label = {
                                            Text(year,
                                                fontFamily = orbitronsFontFamily,
                                                fontWeight = FontWeight.Light)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // مدة الفيلم
                    item {
                        Column {
                            Text(
                                "Duration: $minDuration - $maxDuration min",
                                color = colors.onBackground,
                                fontFamily = orbitronsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Column {
                                Text(
                                    "Min: $minDuration min",
                                    color = colors.secondary,
                                    fontFamily = orbitronsFontFamily,
                                    fontSize = 14.sp
                                )
                                Slider(
                                    value = minDuration.toFloat(),
                                    onValueChange = { minDuration = it.toInt().coerceAtMost(maxDuration) },
                                    valueRange = 0f..300f,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = SliderDefaults.colors(
                                        thumbColor = colors.primary,
                                        activeTrackColor = colors.primary
                                    )
                                )

                                Text(
                                    "Max: $maxDuration min",
                                    color = colors.secondary,
                                    fontFamily = orbitronsFontFamily,
                                    fontSize = 14.sp
                                )
                                Slider(
                                    value = maxDuration.toFloat(),
                                    onValueChange = { maxDuration = it.toInt().coerceAtLeast(minDuration) },
                                    valueRange = 0f..300f,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = SliderDefaults.colors(
                                        thumbColor = colors.primary,
                                        activeTrackColor = colors.primary
                                    )
                                )
                            }
                        }
                    }
                }

                // أزرار التطبيق والإلغاء
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
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
                            onApplyFilters(
                                FilterOptions(
                                    categories = selectedCategories,
                                    minRating = minRating.toDouble(),
                                    year = selectedYear,
                                    minDuration = minDuration,
                                    maxDuration = maxDuration
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                    ) {
                        Text(
                            "Apply Filters",
                            fontFamily = orbitronsFontFamily,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }
    }
}

data class FilterOptions(
    val categories: Set<String> = emptySet(),
    val minRating: Double = 0.0,
    val year: String = "",
    val minDuration: Int = 0,
    val maxDuration: Int = 300
)

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }

        var x = 0
        var y = 0
        var maxHeight = 0

        placeables.forEach { placeable ->
            if (x + placeable.width > constraints.maxWidth) {
                x = 0
                y += maxHeight + 8.dp.roundToPx()
                maxHeight = 0
            }

            maxHeight = maxOf(maxHeight, placeable.height)
            x += placeable.width + 8.dp.roundToPx()
        }

        layout(
            width = constraints.maxWidth,
            height = y + maxHeight
        ) {
            x = 0
            y = 0
            maxHeight = 0

            placeables.forEach { placeable ->
                if (x + placeable.width > constraints.maxWidth) {
                    x = 0
                    y += maxHeight + 8.dp.roundToPx()
                    maxHeight = 0
                }

                placeable.placeRelative(x, y)
                maxHeight = maxOf(maxHeight, placeable.height)
                x += placeable.width + 8.dp.roundToPx()
            }
        }
    }
}