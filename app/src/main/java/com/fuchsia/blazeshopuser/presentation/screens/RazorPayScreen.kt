package com.fuchsia.blazeshopuser.presentation.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.fuchsia.blazeshopuser.MainActivity
import com.fuchsia.blazeshopuser.R
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun RazorPayScreen(
    productId: String,
    productImageUrl: String,
    productName: String,
    totalPrice: String,
    productCount: String,
    email: String,
    firstName: String,
    lastName: String,
    address: String,
    city: String,
    pinCode: String,
    phoneNumber: String,
    navController: NavController,
    firebaseAuth: FirebaseAuth,
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),

) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxSize()
            .padding(10.dp)
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
                text = "Payment Information",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        ProductSummaryCard(
            productImageUrl,
            productName,
            productCount,
            totalPrice,
            navController,
            productId
        )


        var selectedPaymentMethod by remember { mutableStateOf("SBI Bank LTD") }
        var selectedBillingAddress by remember { mutableStateOf("Same as shipping address") }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Payment Method", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("All transactions are secure and encrypted.", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))


        PaymentMethodCard(
            selectedPaymentMethod,
            onPaymentMethodSelected = { selectedPaymentMethod = it })


        Spacer(modifier = Modifier.height(16.dp))
        Text("Billing Address", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(8.dp))

        BillingAddressCard(
            selectedBillingAddress,
            onBillingAddressSelected = { selectedBillingAddress = it })

        Spacer(modifier = Modifier.weight(1f))

        val paymentId = ""

        PayNowButton(
            context,
            paymentId,
            productId,
            productImageUrl,
            productName,
            totalPrice,
            email,
            firstName,
            lastName,
            address,
            city,
            pinCode,
            phoneNumber,
            navController
        )
    }
}


@Composable
fun ProductSummaryCard(
    productImageUrl: String,
    productName: String,
    productCount: String,
    totalPrice: String,
    navController: NavController,
    productId: String
) {
    androidx.compose.material.Card {
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
                        .clip(RoundedCornerShape(8.dp)),

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
            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Shipping")
                Text("Free")
            }
            Divider()
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


@Composable
fun PaymentMethodCard(selectedPaymentMethod: String, onPaymentMethodSelected: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().border(
            width = 1.dp,
            color = Color(0xFFF68B8B),
            shape = RoundedCornerShape(8.dp)
        ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(5.dp)) {

            PaymentMethodItem(
                methodName = "SBI Bank LTD",
                isSelected = selectedPaymentMethod == "SBI Bank LTD",
                onSelect = { onPaymentMethodSelected("SBI Bank LTD") },
                imageResId = "visa_master_rupee.png"

            )
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ){
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = Color.LightGray
                )
            }

            Text(
                "After clicking 'Pay now', you will be redirected to SBI Bank LTD to complete your purchase securely.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Divider()

            PaymentMethodItem(
                methodName = "Cash on Delivery (COD)",
                isSelected = selectedPaymentMethod == "Cash on Delivery (COD)",
                onSelect = { onPaymentMethodSelected("Cash on Delivery (COD)") }

            )
        }
    }
}

@Composable
fun PaymentMethodItem(
    methodName: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    imageResId: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = onSelect)
        Text(methodName, modifier = Modifier.padding(start = 8.dp), fontSize = 14.sp)
        Spacer(modifier = Modifier.weight(1f))
        if (imageResId != null) {
            Image(
                painter = painterResource(id = R.drawable.cards),
                contentDescription = "Payment Icon",
                modifier = Modifier.height(30.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}


@Composable
fun BillingAddressCard(selectedBillingAddress: String, onBillingAddressSelected: (String) -> Unit) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            BillingAddressItem(
                addressName = "Same as shipping address",
                isSelected = selectedBillingAddress == "Same as shipping address",
                onSelect = { onBillingAddressSelected("Same as shipping address") }
            )
            BillingAddressItem(
                addressName = "Use a different billing address",
                isSelected = selectedBillingAddress == "Use a different billing address",
                onSelect = { onBillingAddressSelected("Use a different billing address") }
            )
        }
    }
}

@Composable
fun BillingAddressItem(addressName: String, isSelected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = onSelect)
        Text(addressName, modifier = Modifier.padding(start = 8.dp), fontSize = 14.sp)
    }
}


@Composable
fun PayNowButton(
    context: Context,
    paymentId: String,
    productId: String,
    productImageUrl: String,
    productName: String,
    totalPrice: String,
    email: String,
    firstName: String,
    lastName: String,
    address: String,
    city: String,
    pinCode: String,
    phoneNumber: String,
    navController: NavController
) {
    Button(
        onClick = {

//            (context as? MainActivity)?.startPayment(
//                paymentId,
//                productId,
//                productImageUrl,
//                productName,
//                totalPrice,
//                email,
//                firstName,
//                lastName,
//                address,
//                city,
//                pinCode,
//                phoneNumber,
//            ) ?: Toast.makeText(context, "Error: Invalid Activity", Toast.LENGTH_LONG).show()
                navController.navigate(Routes.PaymentSuccessScreen)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF68B8B)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Place Order", color = Color.White, fontSize = 16.sp)
    }
}

