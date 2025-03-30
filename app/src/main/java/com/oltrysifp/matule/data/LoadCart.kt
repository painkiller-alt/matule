package com.oltrysifp.matule.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.oltrysifp.matule.activities.Cart
import com.oltrysifp.matule.models.CartRecord
import com.oltrysifp.matule.models.FavoriteRecord
import com.oltrysifp.matule.models.User
import com.oltrysifp.matule.util.log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

@Composable
fun LoadCart(
    supabase: SupabaseClient?,
    cart: MutableList<CartRecord>,
    user: User?,
    onLoad: () -> Unit
) {
    LaunchedEffect(user) {
        if (user?.email != null) {
            supabase?.let {
                val cartResult = supabase.from("cart_items").select {
                    filter {
                        eq("user_email", user.email)
                    }
                }.decodeList<CartRecord>()

                log(cartResult)

                cart.clear()
                cart.addAll(cartResult)
                onLoad()
            }
        }
    }
}