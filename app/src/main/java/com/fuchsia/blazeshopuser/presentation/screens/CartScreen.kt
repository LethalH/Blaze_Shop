package com.fuchsia.blazeshopuser.presentation.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel
import kotlin.math.roundToInt

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {

    val cartState = viewModel.getCartState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getCart()

    }

    val deleteCartItemState = viewModel.deleteCartState.collectAsState()
    when {
        deleteCartItemState.value.isLoading -> {
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

        deleteCartItemState.value.error?.isNotBlank() == true -> {

        }

        deleteCartItemState.value.isSuccess != null -> {
            LaunchedEffect(key1 = Unit) {
                viewModel.getCart()
            }
        }
    }

    when {
        cartState.value.isLoading -> {

        }

        cartState.value.error?.isNotBlank() == true -> {
        }

        cartState.value.isSuccess != null -> {

            val cartList = cartState.value.isSuccess ?: emptyList()

            val totalPrice = cartList.sumOf {
                (it.discountedPrice.toFloat() * it.productCount.toInt()).roundToInt()
            }

            if (cartList.isNotEmpty()) {
                Text(
                    modifier = modifier.padding(top = 10.dp, start = 15.dp),
                    text = "Shopping Cart",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                Box {

                    Column(
                        modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                    ) {

                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("Items", fontWeight = FontWeight.Bold, color = Color(0xFFF68B8B))
                            Spacer(modifier = Modifier.weight(1f))
                            Text("Price", fontWeight = FontWeight.Bold, color = Color(0xFFF68B8B))
                            Spacer(modifier = Modifier.width(10.dp))

                        }


                        LazyColumn {
                            items(cartList.size) { index ->
                                CartItemRow(
                                    viewModel = viewModel,
                                    navController = navController,
                                    cartList[index].productId,
                                    cartList[index].productImageUrl,
                                    cartList[index].productName,
                                    cartList[index].productPrice,
                                    cartList[index].discountedPrice,
                                    cartList[index].totalPrice,
                                    cartList[index].productCount

                                )

                            }

                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp)
                                .height(2.dp)
                                .background(
                                    color = Color(0xFFF68B8B),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {}
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                "Sub Total =",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF68B8B)
                            )

                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Rs. $totalPrice", fontWeight = FontWeight.Bold,
                                color = Color(0xFFF68B8B)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    ) {
                        Row(
                            Modifier
                                .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable {
                                    navController.navigate(
                                        Routes.PaymentDetailsScreen(
                                            productId = cartList[0].productId,
                                            productImageUrl = cartList[0].productImageUrl,
                                            productName = cartList[0].productName,
                                            totalPrice = totalPrice.toString(),
                                            productCount = cartList[0].productCount,
                                        )
                                    )
                                }
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF68B8B)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.Center

                        ) {

                            Text(
                                text = "Buy Now",
                                color = Color.White,
                            )
                        }

                    }
                }

            } else {
                Column {
                    Text(
                        modifier = modifier.padding(10.dp),
                        text = "Shopping Cart",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Box(
                        modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Cart is empty")
                    }
                }

            }
        }

    }

}

@Composable
fun CartItemRow(
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController,
    productId: String,
    productImageUrl: String,
    productName: String,
    productPrice: String,
    discountedPrice: String,
    totalPrice: String,
    productCount: String
) {
    Card (modifier = Modifier.padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F9FC)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ){
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .border(1.dp, Color(0xFFF68B8B), RoundedCornerShape(8.dp))
                        .size(90.dp)
                ) {
                    AsyncImage(
                        model = productImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate(Routes.ProductDetailsScreen(productId))
                            },
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = productName,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Price: Rs. $productPrice",
                        textDecoration = TextDecoration.LineThrough,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "Size: M",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Color: ",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(color = Color.Green, shape = CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = discountedPrice,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "x$productCount",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Total: Rs. $totalPrice",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(30.dp)
                            .align(Alignment.End)
                            .border(
                                1.dp,
                                Color.Gray,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                viewModel.deleteCart(productId)
                            }
                    ) {
                        Text(
                            text = "Remove",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
