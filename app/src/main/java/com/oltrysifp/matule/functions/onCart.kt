package com.oltrysifp.matule.functions

import androidx.compose.runtime.MutableState
import com.oltrysifp.matule.models.CartRecord
import com.oltrysifp.matule.models.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun onCart(
    coroutine: CoroutineScope,
    user: MutableState<User?>,
    supabase: SupabaseClient?,
    productID: Int?
) {
    coroutine.launch {
        user.value?.let { user ->
            if (supabase != null && productID != null) {
                supabase.from("cart_items").insert(
                    CartRecord(
                        productId = productID,
                        userEmail = user.email,
                        count = 1
                    )
                )
            }
        }
    }
}