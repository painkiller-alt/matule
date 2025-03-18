package com.oltrysifp.matule.data

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import com.oltrysifp.matule.BuildConfig
import com.oltrysifp.matule.models.Product
import com.oltrysifp.matule.models.ProductRecord
import com.oltrysifp.matule.util.log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import io.ktor.util.Identity.decode
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun LoadProducts(
    supabase: SupabaseClient?,
    products: MutableList<Product>,
    onLoad: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        if (supabase != null) {
            val productsResult = supabase.postgrest.from("products").select()
                .decodeList<ProductRecord>()

            val imageNames = mutableListOf<String>()

            for (product in productsResult) {
                imageNames.add("shoe${product.id}.png")
            }

            val bucket = supabase.storage["images"]
            val urls = bucket.createSignedUrls(
                expiresIn = 10.minutes,
                imageNames
            )

            products.clear()
            productsResult.forEachIndexed { index, product ->
                log("shoe${product.id}.png")
                val imageUri = urls[index]

                products.add(
                    Product(
                        name = product.name,
                        descr = product.descr,
                        price = product.price,
                        image = imageUri.signedURL,
                        id = product.id
                    )
                )
            }
            onLoad()
        }
    }
}