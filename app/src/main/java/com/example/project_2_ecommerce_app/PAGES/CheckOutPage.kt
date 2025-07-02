package com.example.project_2_ecommerce_app.PAGES

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.project_2_ecommerce_a.GlobNavigation.navController
import com.example.project_2_ecommerce_app.model.OwnProducts
import com.example.project_2_ecommerce_app.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MutableCollectionMutableState", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CheckOutPage(modifier: Modifier = Modifier) {
    var user by remember { mutableStateOf(User()) }
    val productList = remember { mutableStateListOf<OwnProducts>() }
    val discount = remember { mutableStateOf(0.0) }
    val tax = remember { mutableStateOf(0.0) }
    val deliveryCharges = remember { mutableStateOf(0.0) }

    val subTotal = remember { mutableStateOf(0.0) }
    val totalAmount = remember { mutableStateOf(0.0) }

    fun calculateSubTotal() {
        subTotal.value = 0.0

        productList.forEach {
            if (it.actualprice?.isNotEmpty() == true) {
                val quantity = user.cartItems[it.id?.trim()] ?: 0
                val price = it.actualprice.toDoubleOrNull() ?: 0.0
                subTotal.value += quantity * price
            }
        }

        discount.value = subTotal.value * 0.1     // 10% discount
        tax.value = subTotal.value * 0.05         // 5% tax
        deliveryCharges.value = 0.0               // No delivery cost

        totalAmount.value =
            (subTotal.value - discount.value) + tax.value + deliveryCharges.value
    }




    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: return@LaunchedEffect)
            .get()
            .addOnCompleteListener { it1 ->
                if (it1.isSuccessful) {
                    val result = it1.result.toObject(User::class.java) ?: return@addOnCompleteListener
                    user = result

                    val productIds = user.cartItems.keys.map { it.trim() }

                    if (productIds.isNotEmpty()) {
                        Firebase.firestore.collection("data")
                            .document("products")
                            .collection("ownproducts")
                            .whereIn("id", productIds)
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val resultProduct = task.result?.toObjects(OwnProducts::class.java)
                                    if (resultProduct != null) {
                                        productList.addAll(resultProduct)
                                        calculateSubTotal()
                                    }
                                }
                            }
                    }
                }
            }
    }

    BackHandler {
        navController.popBackStack() // Go back to previous screen
    }
    Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = {
                    Text(
                        "Checkout",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack, contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = Color(0xFF3F51B5),
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Order Summary",
                    style = MaterialTheme.typography.h4
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card for Pricing Breakdown
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        PriceRow(label = "Subtotal", value = subTotal.value)
                        PriceRow(label = "Discount (10%)", value = -discount.value)
                        PriceRow(label = "Tax (5%)", value = tax.value)
                        PriceRow(label = "Delivery", value = deliveryCharges.value)

                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        PriceRow(
                            label = "Total Amount",
                            value = totalAmount.value,
                            isBold = true
                        )
                    }
                }
            }

            // Place Order Button
            Button(
                onClick = { /* TODO: Handle order */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                enabled = user.cartItems.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = MaterialTheme.colors.onPrimary
                )
            ) {
                Text("Place Order", style = MaterialTheme.typography.h5)
            }
        }
    }



}
@Composable
fun PriceRow(label: String, value: Double, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.h5 else MaterialTheme.typography.h6
        )
        Text(
            text = "₹%.2f".format(value),
            style = if (isBold) MaterialTheme.typography.h4 else MaterialTheme.typography.h6
        )
    }
}
