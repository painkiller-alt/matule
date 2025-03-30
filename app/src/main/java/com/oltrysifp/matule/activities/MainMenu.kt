package com.oltrysifp.matule.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.composable.DefaultLoader
import com.oltrysifp.matule.composable.EdgeToEdgeConfig
import com.oltrysifp.matule.composable.NonLazyGrid
import com.oltrysifp.matule.composable.ProductCard
import com.oltrysifp.matule.composable.SideMenuContent
import com.oltrysifp.matule.composable.navigation.NavBar
import com.oltrysifp.matule.composable.navigation.NavigationItem
import com.oltrysifp.matule.composable.navigation.navigate
import com.oltrysifp.matule.data.LoadCart
import com.oltrysifp.matule.data.LoadFavorite
import com.oltrysifp.matule.data.LoadProducts
import com.oltrysifp.matule.data.LoadUser
import com.oltrysifp.matule.functions.onCart
import com.oltrysifp.matule.functions.onFavorite
import com.oltrysifp.matule.models.CartRecord
import com.oltrysifp.matule.models.FavoriteRecord
import com.oltrysifp.matule.models.Product
import com.oltrysifp.matule.models.User
import com.oltrysifp.matule.util.DataStoreManager
import com.oltrysifp.matule.util.MyAppViewModel
import com.oltrysifp.matule.util.getSupabaseInstance
import com.oltrysifp.matule.util.log
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.exception.AuthSessionMissingException
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val supabase = getSupabaseInstance(this)

        setContent {
            val navController = rememberNavController()

            val sideMenuOpened = remember { mutableStateOf(false) }

            val selectedItem = remember { mutableIntStateOf(0) }
            val currentRoute = remember { mutableStateOf(NavigationItem.Home.route) }
            val items = listOf(
                NavigationItem.Home,
                NavigationItem.Favourite,
                NavigationItem.Notifications,
                NavigationItem.Profile
            )
            items.forEachIndexed { index, navigationItem ->
                if (navigationItem.route == currentRoute.value) {
                    selectedItem.intValue = index
                }
            }

            EdgeToEdgeConfig(this)
            MatuleTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    SideMenu(
                        {
                            Box(
                                Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(innerPadding)
                            ) {
                                NavigationContents(
                                    navHostController = navController,
                                    currentRoute = currentRoute,
                                    routes = items,
                                    selectedItem = selectedItem,
                                    sideMenuOpened = sideMenuOpened
                                )
                            }
                        },
                        sideMenuOpened
                    ) {
                        val context = LocalContext.current
                        val signOutCoroutine = rememberCoroutineScope()
                        SideMenuContent(innerPadding) {
                            val intent = Intent(context, Splash::class.java)

                            signOutCoroutine.launch {
                                try {
                                    if (supabase != null) {
                                        supabase.auth.signOut()
                                    }
                                } catch (e: AuthSessionMissingException) {

                                }

                                this@MainMenu.finish()
                                context.startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationContents(
    navHostController: NavHostController,
    routes: List<NavigationItem>,
    selectedItem: MutableIntState,
    currentRoute: MutableState<String>,
    sideMenuOpened: MutableState<Boolean>
) {
    Box {
        NavHost(
            navController = navHostController,
            startDestination = "home",
            enterTransition = { fadeIn(animationSpec = tween(200)) },
            exitTransition = { fadeOut(animationSpec = tween(200)) },
        ) {
            composable("home") {
                MainMenuScreen(sideMenuOpened)
            }

            composable("favourite") {
                Box {
                    FavoriteScreen {
                        navigate(
                            currentRoute = currentRoute,
                            navController = navHostController,
                            routes = routes,
                            selectedItem = selectedItem,
                            index = 0
                        )
                    }
                }
            }

            composable("notifications") {
                NotificationsScreen(
                    sideMenuOpened
                )
            }

            composable("profile") {
                ProfileScreen(
                    sideMenuOpened
                )
            }
        }

        NavBar(
            currentRoute = currentRoute,
            navController = navHostController,
            routes = routes,
            selectedItem = selectedItem
        )
    }
}

@Preview
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainMenuScreen(
    sideMenuOpened: MutableState<Boolean> = mutableStateOf(false),
    viewModel: MyAppViewModel = viewModel()
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
        onFailure = {
        }
    )
    var favoriteLoaded by remember { mutableStateOf(false) }
    val favorite = remember { mutableStateListOf<FavoriteRecord>() }

    var cartLoaded by remember { mutableStateOf(false) }
    val cart = remember { mutableStateListOf<CartRecord>() }

    LoadProducts(supabase, products) { productsLoaded = true }
    LoadFavorite(supabase, favorite, user.value) { favoriteLoaded = true }
    LoadCart(supabase, cart, user.value) { cartLoaded = true }

    val queue = remember { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                Modifier.fillMaxWidth(0.95f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) { TopControls(sideMenuOpened) }

                Spacer(Modifier.padding(10.dp))

                Search(queue)

                Spacer(Modifier.padding(2.dp))

                Text("Категории", fontSize = 20.sp, modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp))
            }

            Spacer(Modifier.padding(4.dp))

            FlowRow(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                val context = LocalContext.current
                SubFilter("Все") { openScopeProducts("Все", false, context) }
                SubFilter("Outdoor") { openScopeProducts("Outdoor", false, context) }
                SubFilter("Tennis") { openScopeProducts("Tennis", false, context) }
                SubFilter("All Shoes") { openScopeProducts("All Shoes", false, context) }
            }

            Spacer(Modifier.padding(4.dp))

            Column(
                Modifier
                    .fillMaxWidth(0.95f)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        )
                ) {
                    val context = LocalContext.current
                    Text("Популярное", fontSize = 20.sp, modifier = Modifier.padding(vertical = 10.dp))
                    Text(
                        "Все",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { openScopeProducts("Популярное", true, context)  }
                    )
                }

                if (productsLoaded && favoriteLoaded && cartLoaded) {
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

                        val isFav = favorite.firstOrNull { favoriteItem -> favoriteItem.productId == product.id }
                        val isFavorite = remember { mutableStateOf(isFav != null) }

                        log(cart)
                        val inCartCheck = cart.firstOrNull {cartItem -> cartItem.productId == product.id }
                        val inCart = remember { mutableStateOf(inCartCheck != null) }

                        ProductCard(
                            product,
                            onCart = {
                                onCart(
                                    coroutine,
                                    user,
                                    supabase,
                                    productID
                                )
                            },
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
                } else {
                    DefaultLoader()
                }

                Spacer(Modifier.padding(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        )
                ) {
                    Text("Акции", fontSize = 20.sp, modifier = Modifier.padding(vertical = 10.dp))
                    Text("Все", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                }

                Image(
                    painterResource(R.drawable.ic_sale),
                    "sale",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.padding(70.dp))
            }
        }
    }
}

@Composable
fun SideMenuOpener(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        IconButton(
            onClick = { onClick() },
            modifier = Modifier
                .size(42.dp)
        ) {
            Image(
                painterResource(R.drawable.ic_menu_hamburger),
                "ic_menu_hamburger",
                modifier = Modifier.size(34.dp)
            )
        }
    }
}

@Composable
fun TopControls(
    sideMenuOpened: MutableState<Boolean>
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
    ) {
        SideMenuOpener {
            sideMenuOpened.value = true
        }
        Text(
            "Главная",
            fontSize = 36.sp
        )
        Box(
            Modifier.padding(10.dp)
        ) {
            val context = LocalContext.current
            IconButton(
                onClick = {
                    val intent = Intent(context, Cart::class.java)

                    context.startActivity(intent)
                },
                modifier = Modifier.size(40.dp)
            ) {
                Image(
                    painterResource(R.drawable.bag),
                    "bag",
                    modifier = Modifier
                        .size(40.dp)
                )
            }

            Canvas(
                modifier = Modifier
                    .offset(
                        x=38.dp,
                        y=10.dp
                    )
            ) {
                drawCircle(
                    color = Palette.Red,
                    radius = 14f
                )
            }
        }
    }
}

@Composable
fun Search(queue: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shadowElevation = 1.dp, // play with the elevation values,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(0.8f),
            color = MaterialTheme.colorScheme.surface
        ) {
            val context = LocalContext.current
            TextField(
                queue.value,
                onValueChange = {
                    queue.value = it },
                enabled = false,
                placeholder = { Text("Поиск", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.search),
                        "search",
                        modifier = Modifier.size(18.dp)
                    ) },
                colors = Palette.textFieldColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.clickable {
                    val intent = Intent(context, Search::class.java)

                    context.startActivity(intent)
                }
            )
        }

        Box(
            Modifier.padding(8.dp)
        ) {
            Surface(
                Modifier
                    .size(58.dp),
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                shadowElevation = 4.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Button (
                        onClick = {},
                        modifier = Modifier
                            .fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) { }

                    Icon(
                        painterResource(R.drawable.sliders),
                        "фильтр",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SubFilter(
    text: String,
    onClick: () -> Unit
) {
    Card(
        Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 8.dp)
        )
    }
}

fun openScopeProducts(
    title: String,
    favouriteIcon: Boolean,
    context: Context
) {
    val intent = Intent(context, ScopeProducts::class.java)
    val b = Bundle()
    b.putString("title", title)
    b.putBoolean("favouriteIcon", favouriteIcon)
    intent.putExtras(b)
    context.startActivity(intent)
}

@Composable
fun SideMenuButton(
    image: Painter,
    text: String,
    onClick: () -> Unit
) {
    Row {
        Spacer(Modifier.padding(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(2.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() }
                .width(230.dp),
        ) {
            Spacer(Modifier.padding(8.dp))

            Icon(
                image,
                text,
                modifier = Modifier
                    .padding(vertical = 14.dp)
                    .size(24.dp),
                tint = Palette.Surface
            )

            Spacer(Modifier.padding(10.dp))

            Text(text, color = Palette.Surface)
        }


        Spacer(Modifier.padding(2.dp))
    }
}