package com.oltrysifp.matule.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.oltrysifp.matule.data.LoadCart
import com.oltrysifp.matule.data.LoadFavorite
import com.oltrysifp.matule.data.LoadProducts
import com.oltrysifp.matule.data.LoadUser
import com.oltrysifp.matule.functions.onFavorite
import com.oltrysifp.matule.models.CartRecord
import com.oltrysifp.matule.models.FavoriteRecord
import com.oltrysifp.matule.models.Product
import com.oltrysifp.matule.models.User
import com.oltrysifp.matule.util.DataStoreManager
import com.oltrysifp.matule.util.MyAppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScopeProducts : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val b = intent.extras ?: return

        val title = b.getString("title").toString()
        val favouriteIcon = b.getBoolean("favouriteIcon")

        setContent {
            MatuleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScopeProductsScreen(
                        innerPadding,
                        { this.finish() },
                        title,
                        favouriteIcon
                    )
                }
            }
        }
    }
}

@Composable
fun ScopeProductsScreen(
    innerPaddingValues: PaddingValues,
    onExit: () -> Unit,
    title: String,
    favouriteIcon: Boolean,
    viewModel: MyAppViewModel = viewModel(),
) {
    val mContext = LocalContext.current
    val supabase = viewModel.supabase
    val dataStoreManager = DataStoreManager(mContext)

    val products = remember { mutableStateListOf<Product>() }
    var productsLoaded by remember { mutableStateOf(false) }

    val user = remember { mutableStateOf<User?>(null) }
    LoadUser(
        dataStoreManager,
        supabase,
        onSuccess = {
            user.value = it
        },
        onFailure = {}
    )
    var favoriteLoaded by remember { mutableStateOf(false) }
    val favorite = remember { mutableStateListOf<FavoriteRecord>() }

    var cartLoaded by remember { mutableStateOf(false) }
    val cart = remember { mutableStateListOf<CartRecord>() }

    LoadProducts(supabase, products) { productsLoaded = true }
    LoadFavorite(supabase, favorite, user.value) { favoriteLoaded = true }
    LoadCart(supabase, cart, user.value) { cartLoaded = true }

    Column(
        modifier = Modifier
            .padding(innerPaddingValues)
            .fillMaxSize()
    ) {
        val favContext = LocalContext.current
        Header(title, { onExit() }) {
            if (favouriteIcon) {
                IconButton(
                    onClick = {
                        val intent = Intent(favContext, Favorite::class.java)
                        favContext.startActivity(intent)
                    },
                    modifier = Modifier
                        .size(42.dp)
                ) {
                    Image(
                        painterResource(R.drawable.heart),
                        "heart",
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
        }

        Column(
            Modifier.verticalScroll(
                rememberScrollState()
            )
        ) {
            if (productsLoaded) {
                NonLazyGrid(
                    columns = 2,
                    itemCount = products.size,
                    verticalGap = 12.dp,
                    horizontalGap = 12.dp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    val product = products[it]
                    val productID = product.id
                    val coroutine = rememberCoroutineScope()

                    val isFav = favorite.firstOrNull { fav -> fav.productId == product.id }
                    val isFavorite = remember { mutableStateOf(isFav != null) }

                    val inCartCheck = cart.firstOrNull {cartItem -> cartItem.productId == product.id }
                    val inCart = remember { mutableStateOf(inCartCheck != null) }

                    ProductCard(
                        Product(
                            name = product.name,
                            descr = product.descr,
                            price = product.price,
                            image = product.image,
                            id = product.id
                        ),
                        onCart = {},
                        onFavorite = { isFavoriteNow ->
                            onFavorite(
                                isFavoriteNow,
                                coroutine,
                                user,
                                supabase,
                                productID
                            ) },
                        isInCart = inCart,
                        isFavourite = isFavorite
                    )
                }
            } else {
                DefaultLoader()
            }
        }
    }
}

