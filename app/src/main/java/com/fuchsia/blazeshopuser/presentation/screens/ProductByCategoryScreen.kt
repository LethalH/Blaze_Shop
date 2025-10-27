package com.fuchsia.blazeshopuser.presentation.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.fuchsia.blazeshopuser.domain.models.ProductModel
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel

@SuppressLint("DefaultLocale")
@Composable
fun ProductByCategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController,
    categoryName: String
) {


    val getAllFlashSaleState = viewModel.getProductByCategoryState.collectAsState()
    val context = LocalContext.current

    val searchTerm = remember { mutableStateOf("") }
    val allProductsList = remember { mutableStateOf(emptyList<ProductModel>()) }

    LaunchedEffect(key1 = Unit) {
        if (getAllFlashSaleState.value.isSuccess == null) {
            viewModel.getProductByCategory(categoryName)
        }
    }
    when {
        getAllFlashSaleState.value.isLoading -> {
            AlertDialog(
                onDismissRequest = { /*TODO*/ },
                text = { CircularProgressIndicator() },
                confirmButton = { /*TODO*/ },
                dismissButton = { /*TODO*/ },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp)
            )

        }

        getAllFlashSaleState.value.error != null -> {
            Toast.makeText(context, getAllFlashSaleState.value.error, Toast.LENGTH_SHORT).show()

        }

        getAllFlashSaleState.value.isSuccess != null -> {
            if (getAllFlashSaleState.value.isSuccess!!.isEmpty()) {
                LaunchedEffect(key1 = Unit) {
                    Toast.makeText(context, "No Product Found", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                allProductsList.value = (getAllFlashSaleState.value.isSuccess ?: emptyList())
            }

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Row(
            modifier = modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = categoryName,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }

        SearchProductBox(
            navController = navController,
            modifier = modifier.padding(10.dp)

        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)

        ) {

            val filteredPdtList = if (searchTerm.value.isNotBlank()) {
                val categoryMatch = allProductsList.value.any { product ->
                    product.productCategory.contains(searchTerm.value, ignoreCase = true)
                }

                if (categoryMatch) {
                    allProductsList.value.filter { product ->
                        product.productCategory.contains(searchTerm.value, ignoreCase = true)
                    }
                } else {
                    allProductsList.value.filter { product ->
                        product.productName.contains(searchTerm.value, ignoreCase = true)
                    }
                }
            } else {
                allProductsList.value
            }
            Log.d("ProductScreen", "Filtered Products: ${filteredPdtList.size}")


            items(filteredPdtList.size) { index ->
                val product = filteredPdtList[index]

                ProductItemView(
                    navController = navController,
                    product.productId,
                    product.productImageUrl,
                    product.productName,
                    product.productPrice,
                    product.discountedPrice
                )

            }

        }

    }

}

@SuppressLint("DefaultLocale")
@Composable
fun ProductItemView(
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
                    Routes.ProductDetailsScreen(
                        productId = productId
                    )
                )

            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = productImageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(180.dp)
                .width(180.dp)
                .border(
                    1.dp,
                    Color(0xFFF68B8B),
                    RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp)),
            //   contentScale = ContentScale.Crop

        )
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    1.dp,
                    Color(0xFFF68B8B),
                    RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .width(250.dp)
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
fun SearchProductBox(
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
            .fillMaxWidth()
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
