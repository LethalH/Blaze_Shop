package com.fuchsia.blazeshopuser.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.fuchsia.blazeshopuser.R
import com.fuchsia.blazeshopuser.domain.models.CartModel
import com.fuchsia.blazeshopuser.domain.models.ProductModel
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel
import kotlin.math.roundToInt

@Composable
fun ProductDetailsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    productId: String
) {

    val state = viewModel.getProductDetailsState.collectAsState()


    val context = LocalContext.current
    val addToCartState = viewModel.addToCartState.collectAsState()
    if (addToCartState.value.isSuccess != null) {
        Toast.makeText(context, "Product Added to Cart.", Toast.LENGTH_SHORT).show()
    }


    LaunchedEffect(key1 = Unit) {
        viewModel.getProductDetails(productId)
    }

    when {
        state.value.isLoading -> {
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

        state.value.error != null -> {
            Text(text = state.value.error!!)
        }

        state.value.isSuccess != null -> {

            val product = state.value.isSuccess!!

            state.value.isSuccess?.let {

                var quantity by remember { mutableIntStateOf(1) }


                Box(
                    modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {
                    val discountedFloat = product.discountedPrice.toFloatOrNull()?: 0f
                    val orderData = CartModel(
                        productId,
                        product.productName,
                        product.productDescription,
                        product.productPrice,
                        product.discountedPrice,
                        totalPrice = (discountedFloat * quantity.toFloat()).roundToInt()
                            .toString(),
                        product.productCategory,
                        product.productImageUrl,
                        quantity.toString(),
                        product.productSize,
                        product.productColor

                    )

                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                            .verticalScroll(rememberScrollState())
                    ) {
                        val pdtData = ProductModel(
                            productId,
                            product.productName,
                            product.productDescription,
                            product.productPrice,
                            product.discountedPrice,
                            product.productCategory,
                            product.productImageUrl,
                            product.productQuantity,
                            product.productRating,
                            product.productSize,
                            product.productColor,
                            isFavourite = true


                        )

                        ProductItem(
                            navController = navController,
                            pdtData,
                            quantity,
                            onQuantityChange = { quantity = it })

                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center
                    )
                    {
                        Row(

                            Modifier
                                .padding(start = 10.dp)
                                .width(180.dp)
                                .height(50.dp)
                                .clickable {
                                    viewModel.addToCart(orderData)
                                }
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF68B8B)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Add to Cart",
                                color = Color.White,
                                modifier = Modifier.padding(start = 50.dp),
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            Modifier
                                .padding(end = 10.dp)
                                .width(180.dp)
                                .height(50.dp)
                                .clickable {
                                    navController.navigate(
                                        Routes.PaymentDetailsScreen(
                                            productId = productId,
                                            productImageUrl = product.productImageUrl,
                                            productName = product.productName,
                                            totalPrice = product.discountedPrice,
                                            productCount = "1",

                                            )
                                    )
                                }
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF68B8B)),
                            verticalAlignment = Alignment.CenterVertically

                        ) {

                            Text(
                                text = "Buy Now",
                                color = Color.White,
                                modifier = Modifier.padding(start = 50.dp),
                            )
                        }
                    }
                }
            }
        }

    }

}


@Composable
fun ProductItem(
    navController: NavController,
    productData: ProductModel,
    quantity: Int, onQuantityChange: (Int) -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    val addToFavouriteState = viewModel.addToFavouriteState.collectAsState()
    val context = LocalContext.current

    Log.d("TAG", "pd: $productData")

    val favState = viewModel.getFavListState.collectAsState()
    val favList = favState.value.isSuccess ?: emptyList()
    var isFavorite by remember { mutableStateOf(favList.any { it.productId == productData.productId }) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getFavList()
    }

    LaunchedEffect(favList) {
        isFavorite = favList.any { it.productId == productData.productId }
    }

    if (addToFavouriteState.value.isSuccess != null) {

        LaunchedEffect(key1 = Unit) {
            Toast.makeText(context, "Product Added to Favourite List.", Toast.LENGTH_SHORT).show()
            viewModel.getFavList()
            isFavorite = false
        }
    }

    val deleteFavState = viewModel.deleteFavState.collectAsState()
    when {

        deleteFavState.value.isSuccess != null -> {
            LaunchedEffect(key1 = Unit) {
                Toast.makeText(context, "Product Removed from Favourite List.", Toast.LENGTH_SHORT)
                    .show()
                viewModel.getFavList()

            }
        }
    }

    when {
        favState.value.isLoading -> {

        }

        favState.value.error?.isNotBlank() == true -> {


        }

        favState.value.isSuccess != null -> {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(15.dp))
                        ) {
                            AsyncImage(
                                model = productData.productImageUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomStart)
                                    .height(80.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                MaterialTheme.colorScheme.surface
                                            ),
                                            startY = 0f,
                                            endY = Float.POSITIVE_INFINITY
                                        )
                                    )
                            )

                            Text(
                                text = productData.productName,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .align(Alignment.BottomStart),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Rs. ${productData.productPrice}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        textDecoration = TextDecoration.LineThrough
                                    ),
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Rs. ${productData.discountedPrice}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xEEEF7F5F)
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))


                            IconButton(
                                onClick = {
                                    if (isFavorite) {
                                        viewModel.deleteFav(productData.productId)
                                    } else {
                                        viewModel.addToFavourite(productData)
                                    }
                                }
                            ) {
                                if (isFavorite) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Favorite",
                                        tint = Color.Red
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.sizelist),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(35.dp)
                                    .padding(end = 8.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .border(1.dp, Color(0xFFF68B8B), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                IconButton(
                                    onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                                    enabled = quantity > 1
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Decrease Quantity",
                                        tint = Color(0xFFF68B8B)
                                    )
                                }

                                Text(
                                    text = quantity.toString(),
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                IconButton(
                                    onClick = { onQuantityChange(quantity + 1) }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase Quantity",
                                        tint = Color(0xFFF68B8B)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = productData.productCategory,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Product Specification",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = productData.productDescription,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(50.dp))
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                }
            }
            
        }

    }


}
