# 🛒 E-Commerce Android App (Jetpack Compose + Firebase + Razorpay)
A complete, modern, and full-stack **E-Commerce Android application** built using **Jetpack Compose**, **Firebase**, and **Razorpay**.
Built with clean architecture, reusable components, and real-time sync

## Features

###  Authentication
- Firebase Email/Password Sign Up & Login
- Persistent login using FirebaseAuth

### 🗂 Category-wise Product Browsing
- Load products dynamically from Firestore subcollections under categories
- Clean UI with real-time product data

### 🛒 Cart Functionality
- Add/Remove items to cart
- Cart count badge in header
- Quantity & price summary
- Checkout page with order summary

### ❤️ Favorite Products
- Mark/unmark products as favorites
- Stored per-user in Firebase
- Optional "Favorites" screen

### 💳 Razorpay Payment Integration
- "Buy Now" and full cart checkout supported
- Razorpay test mode SDK integrated
- Post-payment success hook to save orders

### 🧾 Order Summary Page
- Lists cart items with name & price
- Total price and "Pay Now" button
- Smooth UI built with Jetpack Compose

---

## 🧑‍💻 Tech Stack

| Layer        | Technology                    |
|--------------|-------------------------------|
| UI           | Jetpack Compose, Material 3   |
| Backend      | Firebase Firestore (NoSQL     |
| Auth         | Firebase Authentication       |
| Payment      | Razorpay Android SDK (test mode) |
| Image Loading| Coil                          |
| Architecture | MVVM + State Hoisting         |



