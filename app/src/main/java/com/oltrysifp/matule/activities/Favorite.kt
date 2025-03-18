package com.oltrysifp.matule.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.composable.DefaultLoader
import com.oltrysifp.matule.composable.Header
import com.oltrysifp.matule.composable.NonLazyGrid
import com.oltrysifp.matule.composable.ProductCard
import com.oltrysifp.matule.composable.toastShort
import com.oltrysifp.matule.data.LoadCart
import com.oltrysifp.matule.data.LoadUser
import com.oltrysifp.matule.data.LoadFavorite
import com.oltrysifp.matule.data.LoadProducts
import com.oltrysifp.matule.functions.onFavorite
import com.oltrysifp.matule.models.CartRecord
import com.oltrysifp.matule.models.FavoriteRecord
import com.oltrysifp.matule.models.Product
import com.oltrysifp.matule.models.User
import com.oltrysifp.matule.util.DataStoreManager
import com.oltrysifp.matule.util.MyAppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Favorite : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatuleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        Modifier.padding(innerPadding)
                    ) {
                        FavoriteScreen { this@Favorite.finish() }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteScreen(
    viewModel: MyAppViewModel = viewModel(),
    onExit: () -> Unit
) {
    val supabase = viewModel.supabase
    val mContext = LocalContext.current
    val dataStoreManager = DataStoreManager(mContext)

    val user = remember { mutableStateOf<User?>(null) }
    LoadUser(
        dataStoreManager,
        supabase,
        onSuccess = {
            user.value = it
        },
        onFailure = {
            toastShort(mContext, "Ошибка")
        }
    )

    var isFavouriteLoaded by remember { mutableStateOf(false) }
    val favorite = remember { mutableStateListOf<FavoriteRecord>() }

    var isCartLoaded by remember { mutableStateOf(false) }
    val cart = remember { mutableStateListOf<CartRecord>() }

    var isProductsLoaded by remember { mutableStateOf(false) }
    val products = remember { mutableStateListOf<Product>() }

    LoadProducts(supabase, products) { isProductsLoaded = true }
    LoadFavorite(supabase, favorite, user.value) { isFavouriteLoaded = true }
    LoadCart(supabase, cart, user.value) { isCartLoaded = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Header("Избранное", { onExit() }) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(42.dp)
                ) {
                    Image(
                        painterResource(R.drawable.heart_red),
                        "heart",
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            if (!isFavouriteLoaded || !isProductsLoaded) {
                DefaultLoader()
            } else {
                NonLazyGrid (
                    columns = 2,
                    itemCount = favorite.size,
                    verticalGap = 12.dp,
                    horizontalGap = 12.dp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    val coroutine = rememberCoroutineScope()
                    val favouriteProductID = favorite[it].productId
                    val product = products.first { product -> product.id == favouriteProductID }
                    val productID = product.id

                    val isFavorite = remember { mutableStateOf(true) }

                    val inCartCheck = cart.firstOrNull {cartItem -> cartItem.productId == product.id }
                    val inCart = remember { mutableStateOf(inCartCheck != null) }

                    ProductCard(
                        product,
                        onCart = {},
                        onFavorite = { isFavoriteNow ->
                            onFavorite(
                                isFavoriteNow,
                                coroutine,
                                user,
                                supabase,
                                productID
                            )
                        },
                        isInCart = inCart,
                        isFavourite = isFavorite
                    )
                }
            }
        }
    }
}