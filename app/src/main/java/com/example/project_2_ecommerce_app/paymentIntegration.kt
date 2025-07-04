package com.example.project_2_ecommerce_app

import android.app.Activity
import com.example.project_2_ecommerce_a.GlobNavigation
import com.razorpay.Checkout
import org.json.JSONObject
import kotlin.math.roundToInt

fun razorpayApiKey() : String{
    return "rzp_test_fYaHcW7i5RQY1Z"

}
fun startPayment(amount: Double, activity: Activity) {
    val checkout = Checkout()
    checkout.setKeyID(razorpayApiKey())

    val amountInPaise = (amount .roundToInt() * 100)

    val options = JSONObject().apply {
        put("name", "Easy Shop")
        put("description", "Test payment")
        put("amount", amountInPaise) // in paise, rounded
        put("currency", "INR")
        put("method", JSONObject().apply {
            put("upi", true)
            put("card", true)
            put("wallet", true)
        })
    }

    checkout.open(activity, options)
}
