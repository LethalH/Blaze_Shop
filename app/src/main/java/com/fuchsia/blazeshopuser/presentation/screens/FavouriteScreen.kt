package com.fuchsia.blazeshopuser.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel

@Composable
fun FavouriteScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {

    val favState = viewModel.getFavListState.collectAsState()

    val deleteFavState = viewModel.deleteFavState.collectAsState()
    when {
        deleteFavState.value.isLoading -> {
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

        deleteFavState.value.error?.isNotBlank() == true -> {

        }

        deleteFavState.value.isSuccess != null -> {
            LaunchedEffect(key1 = Unit) {
                viewModel.getFavList()

            }
        }
    }


    LaunchedEffect(key1 = Unit) {
        viewModel.getFavList()

    }

    when {
        favState.value.isLoading -> {

        }

        favState.value.error?.isNotBlank() == true -> {
        }

        favState.value.isSuccess != null -> {


            Column(
                modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                Text(
                    modifier = modifier.padding(top = 10.dp, start = 15.dp, bottom = 10.dp),
                    text = "Favourite Products",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge                )

                if (favState.value.isSuccess?.isEmpty() == false) {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = modifier
                    ) {
                        val favList = favState.value.isSuccess ?: emptyList()

                        items(favList.size) { index ->
                            FavProductItem(
                                viewModel = viewModel,
                                navController = navController,
                                favList[index].productId,
                                favList[index].productImageUrl,
                                favList[index].productName,
                                favList[index].productPrice,
                                favList[index].discountedPrice,
                            )
                        }

                    }
                } else {
                    Box(
                        modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No Favourite Products")
                    }

                }
            }

        }

    }
}


@SuppressLint("DefaultLocale")
@Composable
fun FavProductItem(
    viewModel: MyViewModel = hiltViewModel(),
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

    Box {


        Column(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    navController.navigate(
                        Routes.ProductDetailsScreen(
                            productId
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
              //  contentScale = ContentScale.Crop

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
                        maxLines = 2
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
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier
                .padding(end = 18.dp, top = 10.dp)
                .clickable {
                    viewModel.deleteFav(productId)

                }
                .align(Alignment.TopEnd)
        )
    }

}
