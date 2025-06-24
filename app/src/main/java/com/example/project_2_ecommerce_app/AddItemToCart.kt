package com.example.project_2_ecommerce_app

import android.widget.Toast
import androidx.compose.runtime.Composable
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


fun AddItemToCart(
    productId: String,
    context: android.content.Context

){
//    we are using this like this because we want to update
//    not using like model
     val userDoc = Firebase.firestore
         .collection("users")
         .document(Firebase.auth.currentUser?.uid ?: "")

     userDoc.get().addOnSuccessListener { documentSnapshot ->
         val cartItems = documentSnapshot.data?.get("cartItems") as? Map<String, Long> ?: emptyMap()
         val currentQuantity = cartItems[productId] ?: 0
         val updatedQuantity = currentQuantity + 1

         val updatedCartItems = mapOf("cartItems.$productId" to updatedQuantity)

//         updating in the firebase
         userDoc.update(updatedCartItems)
             .addOnCompleteListener {
                 if (it.isSuccessful) {
//                     onAddToCart(productId)
                     Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show()
                 } else {
                     // Handle the error
                     Toast.makeText(context, "Failed to add item to cart", Toast.LENGTH_SHORT).show()




             }
         }

     }
}