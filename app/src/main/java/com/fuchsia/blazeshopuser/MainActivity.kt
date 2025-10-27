package com.fuchsia.blazeshopuser

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fuchsia.blazeshopuser.common.ONE_SIGNAL_ID
import com.fuchsia.blazeshopuser.common.RAZORPAY_API_PATH
import com.fuchsia.blazeshopuser.domain.models.OrderModel
import com.fuchsia.blazeshopuser.presentation.nav.App
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel
import com.fuchsia.blazeshopuser.ui.theme.BlazeShopUserTheme
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.OneSignal
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid ?: ""
    private var currentOrderDetails: OrderModel? = null

    lateinit var navController: NavController

    lateinit var viewModel: MyViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        OneSignal.initWithContext(this, ONE_SIGNAL_ID)

        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(false)
        }

        enableEdgeToEdge()

        Checkout.preload(applicationContext)
        val co = Checkout()
        co.setKeyID(RAZORPAY_API_PATH)

        setContent {
            BlazeShopUserTheme {
                viewModel = hiltViewModel()
                navController = rememberNavController()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) { innerPadding ->
                    val createOrderState = viewModel.createOrderState.collectAsState()

                    App(
                        modifier = Modifier.padding(innerPadding),
                        firebaseAuth,
                        createOrderState
                    )

                }
            }
        }
    }

    fun startPayment(
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
    ) {
        val activity: Activity = this
        val co = Checkout()

        try {
            currentOrderDetails = OrderModel(
                paymentId = paymentId,
                userId = userId,
                productId = productId,
                productName = productName,
                totalPrice = totalPrice,
                productImageUrl = productImageUrl,
                email = email,
                firstName = firstName,
                lastName = lastName,
                address = address,
                city = city,
                pinCode = pinCode,
                phoneNumber = phoneNumber,
                date = System.currentTimeMillis().toString()
            )

            val options = JSONObject()
            options.put("name", "Blaze Shop")
            options.put("description", "Payment for $productName")
            options.put("image", R.drawable.logo)
            options.put("theme.color", "#F68B8B")
            options.put("currency", "INR")
            options.put("amount", totalPrice)

            co.open(activity, options)

        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {

        currentOrderDetails?.let { order ->
            val updatedOrder = order.copy(paymentId = razorpayPaymentId ?: "")

            Log.d("MainActivity", "Updated Order $updatedOrder")

            val jsonOrder = Json.encodeToString(updatedOrder)
            viewModel.createOrder(jsonOrder)

        }

    }

    override fun onPaymentError(errorCode: Int, errorMessage: String?, paymentData: PaymentData?) {
        Toast.makeText(this, "Payment Failed.", Toast.LENGTH_LONG).show()
    }
}
