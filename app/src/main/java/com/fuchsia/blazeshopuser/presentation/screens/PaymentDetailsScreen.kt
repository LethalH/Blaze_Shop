package com.fuchsia.blazeshopuser.presentation.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.fuchsia.blazeshopuser.presentation.nav.Routes

@Composable
fun PaymentDetailsScreen(
    productId: String,
    productImageUrl: String,
    productName: String,
    totalPrice: String,
    productCount: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TopBar(
            navController = navController
        )
        OrderSummary(
            productId = productId,
            productImageUrl = productImageUrl,
            productName = productName,
            totalPrice = totalPrice,
            productCount = productCount,
            navController = navController
        )
        ShippingAddress(
            productId = productId,
            productImageUrl = productImageUrl,
            productName = productName,
            totalPrice = totalPrice,
            productCount = productCount,
            navController = navController
        )
    }
}

@Composable
fun TopBar(
    navController: NavController
) {
    Row(
        modifier = Modifier.padding(start = 5.dp),
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
            text = "Shipping Details",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
    }

}

@Composable
fun OrderSummary(
    productId: String,
    productImageUrl: String,
    productName: String,
    totalPrice: String,
    productCount: String,
    navController: NavController
) {

    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = productImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            navController.navigate(Routes.ProductDetailsScreen(productId))
                        },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        productName,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Quantity: $productCount", fontSize = 12.sp)
                }
                Text("Rs. $totalPrice", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Shipping")
                Text("Free")
            }
            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", fontWeight = FontWeight.Bold)
                Text("Rs. $totalPrice", fontWeight = FontWeight.Bold)
            }
        }
    }

}


@SuppressLint("UnrememberedMutableState")
@Composable
fun ShippingAddress(
    productId: String,
    productImageUrl: String,
    productName: String,
    totalPrice: String,
    productCount: String,
    navController: NavController
) {
    val email = remember { mutableStateOf("") }
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val pinCode = remember { mutableStateOf("") }


    Column {
        Text("Shipping Information", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            label = {
                Text(
                    text = "Email"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Email") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color(0xFFEBF5FC),
                unfocusedContainerColor = Color(0xFFEBF5FC),
                focusedIndicatorColor = Color(0xFFF68B8B),
                unfocusedIndicatorColor = Color(0xFFF68B8B),
            )

        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                value = firstName.value,
                onValueChange = {
                    firstName.value = it
                },

                modifier = Modifier.weight(1f),
                placeholder = { Text("First Name") },
                label = { Text("First Name") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(0xFFEBF5FC),
                    unfocusedContainerColor = Color(0xFFEBF5FC),
                    focusedIndicatorColor = Color(0xFFF68B8B),
                    unfocusedIndicatorColor = Color(0xFFF68B8B),
                )
            )
            OutlinedTextField(
                value = lastName.value,
                onValueChange = {
                    lastName.value = it
                },
                label = {
                    Text(
                        text = "Last Name"
                    )
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Last Name") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(0xFFEBF5FC),
                    unfocusedContainerColor = Color(0xFFEBF5FC),
                    focusedIndicatorColor = Color(0xFFF68B8B),
                    unfocusedIndicatorColor = Color(0xFFF68B8B),
                )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = address.value,
            onValueChange = {
                address.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = "Address"
                )
            },
            placeholder = { Text("Address") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color(0xFFEBF5FC),
                unfocusedContainerColor = Color(0xFFEBF5FC),
                focusedIndicatorColor = Color(0xFFF68B8B),
                unfocusedIndicatorColor = Color(0xFFF68B8B),
            )
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = city.value,
                onValueChange = {
                    city.value = it
                },
                label = {
                    Text(
                        text = "City"
                    )
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("City") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(0xFFEBF5FC),
                    unfocusedContainerColor = Color(0xFFEBF5FC),
                    focusedIndicatorColor = Color(0xFFF68B8B),
                    unfocusedIndicatorColor = Color(0xFFF68B8B),
                )

            )
            OutlinedTextField(
                value = pinCode.value,
                onValueChange = {
                    pinCode.value = it
                },
                label = {
                    Text(
                        text = "Postal Code"
                    )
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Postal Code") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(0xFFEBF5FC),
                    unfocusedContainerColor = Color(0xFFEBF5FC),
                    focusedIndicatorColor = Color(0xFFF68B8B),
                    unfocusedIndicatorColor = Color(0xFFF68B8B),
                )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = {
                phoneNumber.value = it
            },
            label = {
                Text(
                    text = "Contact Number"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Contact Number") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color(0xFFEBF5FC),
                unfocusedContainerColor = Color(0xFFEBF5FC),
                focusedIndicatorColor = Color(0xFFF68B8B),
                unfocusedIndicatorColor = Color(0xFFF68B8B),
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        ShippingMethod()
        Spacer(modifier = Modifier.height(4.dp))

        val context = LocalContext.current
        Button(
            onClick = {
                if (
                    email.value.isNotEmpty() || firstName.value.isNotEmpty() || lastName.value.isNotEmpty() ||
                    phoneNumber.value.isNotEmpty() || address.value.isNotEmpty() || city.value.isNotEmpty() ||
                    pinCode.value.isNotEmpty()

                ) {
                    navController.navigate(
                        Routes.RazorPayScreen(
                            productId = productId,
                            productImageUrl = productImageUrl,
                            productName = productName,
                            totalPrice = totalPrice,
                            productCount = productCount,
                            email = email.value,
                            firstName = firstName.value,
                            lastName = lastName.value,
                            address = address.value,
                            city = city.value,
                            pinCode = pinCode.value,
                            phoneNumber = phoneNumber.value
                        )
                    )
                } else {

                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()


                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF68B8B),
            ),
            shape = RoundedCornerShape(8.dp)

        ) {
            Text(
                "Continue to Shipping",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

    }
}

@Composable
fun ShippingMethod() {
    Column {
        Text("Shipping Method", fontWeight = FontWeight.Bold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Select standard delivery */ }
        ) {
            RadioButton(selected = true, onClick = {})
            Text("Standard FREE delivery over Rs. 500")
            Spacer(modifier = Modifier.weight(1f))
            Text("Free", fontWeight = FontWeight.Bold)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Select COD */ }
        ) {
            RadioButton(selected = false, onClick = {})
            Column {
                Text("Cash on delivery over Rs. 1000 (Free Delivery, COD processing fee only)")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("100", fontWeight = FontWeight.Bold)
        }
    }
}
