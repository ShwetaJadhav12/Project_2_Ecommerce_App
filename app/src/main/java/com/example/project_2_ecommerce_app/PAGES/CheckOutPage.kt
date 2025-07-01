package com.example.project_2_ecommerce_app.PAGES

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.project_2_ecommerce_app.model.OwnProducts
import com.example.project_2_ecommerce_app.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@SuppressLint("MutableCollectionMutableState")
@Composable
fun CheckOutPage(modifier: Modifier = Modifier) {
    var user by remember { mutableStateOf(User()) }
    val productList = remember { mutableStateListOf<OwnProducts>() }
    val subTotal = remember { mutableStateOf(0.0) }

    fun calculateSubTotal() {
        subTotal.value = 0.0
        productList.forEach {
            if (it.actualprice != null) {
                val quantity = user.cartItems[it.id] ?: 0
                val price = it.actualprice.toDoubleOrNull() ?: 0.0
                val totalPrice = quantity * price
                subTotal.value += totalPrice



            }



        }
    }


    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: return@LaunchedEffect)
            .get()
            .addOnCompleteListener { it1 ->
                if (it1.isSuccessful) {
                    val result = it1.result.toObject(User::class.java) ?: return@addOnCompleteListener
                    user = result

                    val productIds = user.cartItems.keys.toList()

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
    Column(
        modifier = modifier
    ) {
        Text(
            text =" ${productList.toList().size.toString()}"
        )
        Text(
            text =" ${subTotal.value.toString()}"
        )
        Text(
            text =" ${user.cartItems.toString()}"
        )


    }

    // Show products here in LazyColumn or however you're rendering
}
