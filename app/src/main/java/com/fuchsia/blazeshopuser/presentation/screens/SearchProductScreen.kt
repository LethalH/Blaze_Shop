package com.fuchsia.blazeshopuser.presentation.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
fun SearchProductScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    val searchProductState = viewModel.searchProductState.collectAsState()
    val searchProductList = remember { mutableStateOf(emptyList<ProductModel>()) }

    BackHandler {
        if (!navController.popBackStack()) {
            navController.navigate(Routes.BottomNavBar) {
                popUpTo(Routes.BottomNavBar) { inclusive = true }
            }
        }
    }


    when {
        searchProductState.value.isLoading -> {
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

        searchProductState.value.error?.isNotBlank() == true -> {
            Toast.makeText(context, searchProductState.value.error, Toast.LENGTH_SHORT).show()
        }

        searchProductState.value.isSuccess != null -> {

            searchProductList.value = (searchProductState.value.isSuccess ?: emptyList())
        }


    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusRequester = remember { FocusRequester() }
        val searchTerm = remember { mutableStateOf("") }

        LaunchedEffect(focusRequester) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }

        Row(
            modifier = modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.clickable {
                    if (!navController.popBackStack()) {
                        navController.navigate(Routes.BottomNavBar) {
                            popUpTo(Routes.BottomNavBar) { inclusive = true }
                        }
                    }

                }
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Search Product",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }


        Row(
            modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Search(
                searchTerm = searchTerm,
                modifier = modifier.focusRequester(focusRequester)
            )
        }

        Spacer(Modifier.padding(top = 10.dp))

        LazyVerticalGrid(
            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            val filteredList = if (searchTerm.value.isNotBlank()) {
                val categoryMatch = searchProductList.value.any { product ->
                    product.productCategory.contains(searchTerm.value, ignoreCase = true)
                }

                if (categoryMatch) {
                    searchProductList.value.filter { product ->
                        product.productCategory.contains(searchTerm.value, ignoreCase = true)
                    }
                } else {
                    searchProductList.value.filter { product ->
                        product.productName.contains(searchTerm.value, ignoreCase = true)
                    }
                }
            } else {
                searchProductList.value
            }

            items(filteredList.size) { index ->
                val product = filteredList[index]

                val productPriceFloat = product.productPrice.toFloatOrNull() ?: 0f
                val discountedPriceFloat = product.discountedPrice.toFloatOrNull() ?: 0f

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
                                Routes.ProductDetailsScreen(productId = product.productId)
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = product.productImageUrl,
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
                                text = product.productName,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1
                            )
                            Row {
                                Text(
                                    text = "Rs. ${product.productPrice}",
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
                                text = "Rs. ${product.discountedPrice}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF68B8B)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Search(
    searchTerm: MutableState<String>,
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current


    OutlinedTextField(
        value = searchTerm.value,
        onValueChange = { searchTerm.value = it },
        modifier = modifier
            .height(52.dp)
            .width(350.dp)
            .focusRequester(remember { FocusRequester() }),
        placeholder = {
            Text("Search")
        },
        singleLine = true,
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color(0xFFEBF5FC),
            unfocusedContainerColor = Color(0xFFEBF5FC),
            focusedIndicatorColor = Color(0xFFF68B8B),
            unfocusedIndicatorColor = Color(0xFFF68B8B),
        ),
        leadingIcon = {
            IconButton(onClick = {
                if (searchTerm.value.isNotBlank()) {
                    searchTerm.value = ""
                }
            }) {
                Icon(
                    imageVector = if (searchTerm.value.isNotBlank()) Icons.Default.Close else Icons.Default.Search,
                    contentDescription = if (searchTerm.value.isNotBlank()) "Clear" else "Search",
                    tint = if (searchTerm.value.isNotBlank()) Color.Gray else Color.Black
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                if (searchTerm.value.isNotBlank()) {
                    viewModel.searchProduct(searchTerm.value)

                }
            }
        )
    )
}
