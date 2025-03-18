package com.oltrysifp.matule.functions

import androidx.compose.runtime.MutableState
import com.oltrysifp.matule.models.FavoriteRecord
import com.oltrysifp.matule.models.User
import com.oltrysifp.matule.util.log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun onFavorite(
    isFavoriteNow: Boolean,
    coroutine: CoroutineScope,
    user: MutableState<User?>,
    supabase: SupabaseClient?,
    productID: Int?
) {
    if (isFavoriteNow) {
        coroutine.launch {
            user.value?.let { user ->
                if (supabase != null && productID != null) {
                    supabase.from("favorite").insert(
                        FavoriteRecord(
                            productId = productID,
                            userId = user.uid
                        )
                    )
                }
            }
        }
    } else {
        coroutine.launch {
            user.value?.let { user ->
                if (supabase != null && productID != null) {
                    supabase.from("favorite").delete {
                        filter {
                            eq("product_id", productID)
                            eq("user_id", user.uid)
                        }
                    }
                }
            }
        }
    }
}