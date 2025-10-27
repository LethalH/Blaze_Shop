package com.fuchsia.blazeshopuser.presentation.screens


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.fuchsia.blazeshopuser.R
import com.fuchsia.blazeshopuser.common.FLASH_SALE
import com.fuchsia.blazeshopuser.domain.models.ProductCategoryModel
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    // Collect states
    val homeCategoryState by viewModel.getHomeCategoryState.collectAsState()
    val bannerState by viewModel.getBannerState.collectAsState()
    val homeFlashSaleState by viewModel.getHomeProductByCategoryState.collectAsState()

    // Load data once
    LaunchedEffect(Unit) {
        viewModel.getHomeCategory()
        viewModel.getHomeFlashSale(FLASH_SALE)
        viewModel.getBanner()
    }

    // Handle errors with LaunchedEffect (runs once per error)
    LaunchedEffect(homeCategoryState.error) {
        homeCategoryState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(bannerState.error) {
        bannerState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(homeFlashSaleState.error) {
        homeFlashSaleState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    // Show loading or content
    when {
        homeFlashSaleState.isLoading -> {
            LoadingIndicator()
        }

        homeFlashSaleState.isSuccess != null &&
                homeCategoryState.isSuccess != null &&
                bannerState.isSuccess != null -> {
            HomeContent(
                modifier = modifier,
                navController = navController,
                categories = homeCategoryState.isSuccess!!,
                banners = bannerState.isSuccess!!,
                flashSaleProducts = homeFlashSaleState.isSuccess!!
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color(0xFFF68B8B))
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun HomeContent(
    modifier: Modifier,
    navController: NavController,
    categories: List<ProductCategoryModel>,
    banners: List<ProductCategoryModel>,
    flashSaleProducts: List<com.fuchsia.blazeshopuser.domain.models.ProductModel>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // Header with search and notification
        HomeHeader(navController = navController, modifier = modifier)

        // Categories section
        CategorySection(
            categories = categories,
            navController = navController,
            modifier = modifier
        )

        // Banner section
        Box(modifier = modifier.height(220.dp)) {
            SliderBanner(
                navController = navController,
                bannerList = banners
            )
        }

        // Flash Sale section
        FlashSaleSection(
            flashSaleProducts = flashSaleProducts,
            navController = navController,
            modifier = modifier
        )
    }
}

@Composable
private fun HomeHeader(
    navController: NavController,
    modifier: Modifier
) {
    Row(
        modifier = modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBox(
            navController = navController,
            modifier = modifier.padding(10.dp)
        )

        Spacer(modifier = modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.notification),
            contentDescription = "Notifications",
            tint = Color(0xFFF68B8B),
            modifier = Modifier
                .padding(end = 10.dp)
                .size(30.dp)
                .clickable {
                    navController.navigate(Routes.NotificationScreen)
                }
        )
    }
}

@Composable
private fun CategorySection(
    categories: List<ProductCategoryModel>,
    navController: NavController,
    modifier: Modifier
) {
    Column {
        Row(modifier = modifier.padding(start = 10.dp, end = 10.dp)) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = modifier.weight(1f))
            Text(
                text = "See more",
                color = Color(0xFFF68B8B),
                modifier = modifier.clickable {
                    navController.navigate(Routes.AllCategoryScreen)
                }
            )
        }

        LazyRow(modifier = Modifier.heightIn(100.dp)) {
            items(categories.size) { index ->
                CategoryItem(
                    categoryImageUrl = categories[index].categoryImageUrl,
                    categoryName = categories[index].categoryName,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun FlashSaleSection(
    flashSaleProducts: List<com.fuchsia.blazeshopuser.domain.models.ProductModel>,
    navController: NavController,
    modifier: Modifier
) {
    Column {
        Row(modifier = modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp)) {
            Text(
                text = "Flash Sale",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = modifier.weight(1f))
            Text(
                text = "See more",
                color = Color(0xFFF68B8B),
                modifier = modifier.clickable {
                    navController.navigate(
                        Routes.ProductByCategoryScreen(categoryName = "Flash Sale")
                    )
                }
            )
        }

        LazyRow(modifier = Modifier.heightIn(100.dp)) {
            items(flashSaleProducts.size) { index ->
                val product = flashSaleProducts[index]
                FlashSaleProductItem(
                    navController = navController,
                    productId = product.productId,
                    productImageUrl = product.productImageUrl,
                    productName = product.productName,
                    productPrice = product.productPrice,
                    discountedPrice = product.discountedPrice
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun FlashSaleProductItem(
    navController: NavController,
    productId: String,
    productImageUrl: String,
    productName: String,
    productPrice: String,
    discountedPrice: String
) {
    val productPriceFloat = productPrice.toFloatOrNull() ?: 0f
    val discountedPriceFloat = discountedPrice.toFloatOrNull() ?: 0f

    val off = if (productPriceFloat > 0) {
        (productPriceFloat - discountedPriceFloat) / productPriceFloat * 100
    } else {
        0f
    }
    val offString = String.format("%.1f", off)

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                navController.navigate(
                    Routes.ProductDetailsScreen(productId = productId)
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = productImageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(150.dp)
                .width(150.dp)
                .border(1.dp, Color(0xFFF68B8B), RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFFF68B8B), RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .width(150.dp)
                    .padding(10.dp)
            ) {
                Text(
                    text = productName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Row {
                    Text(
                        text = "Rs. $productPrice",
                        textDecoration = TextDecoration.LineThrough,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "$offString% off",
                        color = Color(0xFFF68B8B),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "Rs. $discountedPrice",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF68B8B)
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    categoryImageUrl: Any?,
    categoryName: Any?,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                navController.navigate(
                    Routes.ProductByCategoryScreen(categoryName.toString())
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = categoryImageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(75.dp)
                .border(1.dp, Color(0xFFF68B8B), CircleShape)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(text = categoryName.toString())
    }
}

@Composable
fun SearchBox(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFFEBF5FC), shape = RoundedCornerShape(25.dp))
            .height(52.dp)
            .clickable {
                navController.navigate(Routes.SearchProductScreen)
            }
            .width(310.dp)
            .border(2.dp, Color(0xFFF68B8B), shape = RoundedCornerShape(25.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Search",
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun SliderBanner(
    navController: NavController,
    bannerList: List<ProductCategoryModel>,
    modifier: Modifier = Modifier
) {
    if (bannerList.isEmpty()) return

    val pagerState = rememberPagerState(initialPage = 0)

    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(2600)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % bannerList.size
            )
        }
    }

    Column {
        HorizontalPager(
            count = bannerList.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 10.dp),
            modifier = modifier
                .height(180.dp)
                .fillMaxWidth()
        ) { page ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .clickable {
                        navController.navigate(
                            Routes.ProductByCategoryScreen(bannerList[page].categoryName)
                        )
                    }
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                AsyncImage(
                    model = bannerList[page].categoryImageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}