package com.example.project_2_ecommerce_app.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FavoritesManager {

    // ✅ Add product to favorites
    fun addToFavorites(productId: String) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val db = Firebase.firestore
        val trimmedId = productId.trim()

        db.collection("users")
            .document(user.uid)
            .collection("favorites")
            .document(trimmedId)
            .set(mapOf("productId" to trimmedId))
    }

    // ✅ Remove product from favorites
    fun removeFromFavorites(productId: String) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val db = Firebase.firestore
        val trimmedId = productId.trim()

        db.collection("users")
            .document(user.uid)
            .collection("favorites")
            .document(trimmedId)
            .delete()
    }

    // ✅ Check if a product is already a favorite
    fun isFavorite(productId: String, callback: (Boolean) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser ?: return callback(false)
        val db = Firebase.firestore
        val trimmedId = productId.trim()

        db.collection("users")
            .document(user.uid)
            .collection("favorites")
            .document(trimmedId)
            .get()
            .addOnSuccessListener { document ->
                callback(document.exists())
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    // ✅ Get all favorite product IDs for current user
    fun getAllFavorites(callback: (List<String>) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser ?: return callback(emptyList())
        val db = Firebase.firestore

        db.collection("users")
            .document(user.uid)
            .collection("favorites")
            .get()
            .addOnSuccessListener { documents ->
                val productIds = documents.mapNotNull { it.getString("productId")?.trim() }
                callback(productIds)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
}
