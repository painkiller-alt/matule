package com.oltrysifp.matule.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.oltrysifp.matule.models.FavoriteRecord
import com.oltrysifp.matule.models.User
import com.oltrysifp.matule.util.log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

@Composable
fun LoadFavorite(
    supabase: SupabaseClient?,
    favourite: MutableList<FavoriteRecord>,
    user: User?,
    onLoad: () -> Unit
) {
    LaunchedEffect(user) {
        if (user?.uid != null) {
            supabase?.let {
                val favouriteResult = supabase.from("favorite").select {
                    filter {
                        eq("user_id", user.uid)
                    }
                }.decodeList<FavoriteRecord>()

                log(favouriteResult)

                favourite.clear()
                favourite.addAll(favouriteResult)
                onLoad()
            }
        }
    }
}