package com.oltrysifp.matule.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.composable.DefaultLoader
import com.oltrysifp.matule.composable.Header
import com.oltrysifp.matule.data.LoadCart
import com.oltrysifp.matule.data.LoadProducts
import com.oltrysifp.matule.data.LoadUser
import com.oltrysifp.matule.getProductImageRequest
import com.oltrysifp.matule.models.CartRecord
import com.oltrysifp.matule.models.Product
import com.oltrysifp.matule.models.User
import com.oltrysifp.matule.util.DataStoreManager
import com.oltrysifp.matule.util.MyAppViewModel
import com.oltrysifp.matule.util.log
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Cart : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatuleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CartScreen(
                        innerPadding
                    ) { this.finish() }
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    innerPaddingValues: PaddingValues,
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
        onFailure = { }
    )

    var isCartLoaded by remember { mutableStateOf(false) }
    val cart = remember { mutableStateListOf<CartRecord>() }

    var isProductsLoaded by remember { mutableStateOf(false) }
    val products = remember { mutableStateListOf<Product>() }

    LoadProducts(supabase, products) { isProductsLoaded = true }
    LoadCart(supabase, cart, user.value) { isCartLoaded = true }

    Column(
        modifier = Modifier
            .padding(innerPaddingValues)
            .fillMaxSize()
    ) {
        Header("Корзина", { onExit() }) { }

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .weight(1f)
        ) {
            Text("3 Товара")

            Spacer(Modifier.padding(4.dp))

            if (!isCartLoaded || !isProductsLoaded) {
                DefaultLoader()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item(
                        key = "<spacer1>"
                    ) { Spacer(Modifier.padding(3.dp)) }
                    items(
                        cart,
                        key = {
                            it.productId
                        }
                    ) { cartItem ->

                        val coroutine = rememberCoroutineScope()
                        val product = products.first { product -> product.id == cartItem.productId }
                        val productID = product.id

                        Box(
                            modifier = Modifier
                                .animateItem()
                        ) {
                            CartProduct(
                                product,
                                {
                                    cart.remove(cartItem)
                                    coroutine.launch {
                                        user.value?.let { user ->
                                            if (supabase != null && productID != null) {
                                                log("del")
                                                supabase.from("cart_items").delete {
                                                    filter {
                                                        eq("user_email", user.email)
                                                        eq("product_id", productID)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                },
                                { newCount ->
                                    coroutine.launch {
                                        user.value?.let { user ->
                                            if (supabase != null && productID != null) {
                                                supabase.from("cart_items").update(
                                                    {
                                                        CartRecord::count setTo newCount
                                                    }
                                                ) {
                                                    filter {
                                                        eq("user_email", user.email)
                                                        eq("product_id", productID)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                    item(
                        key = "<spacer2>"
                    ) { Spacer(Modifier.padding(6.dp)) }
                }
            }
        }

        Surface(
            modifier = Modifier.weight(0.45f)
        ) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Сумма")
                    Text("₽100.46", fontSize = 12.sp)
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Доставка")
                    Text("₽20.4", fontSize = 12.sp)
                }

                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 18f), 0f)
                Canvas(
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(vertical = 4.dp)
                ) {
                    drawLine(
                        color = Palette.Text,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        pathEffect = pathEffect
                    )
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Итого")
                    Text("₽120.4", fontSize = 12.sp)
                }

                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .height(54.dp)
                            .fillMaxWidth(),
                        onClick = {  },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Оформить заказ",
                            fontSize = 16.sp,
                            color = Palette.Surface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartProduct(
    product: Product,
    onDelete: () -> Unit,
    onCount: (Int) -> Unit
) {
    var offset by remember { mutableFloatStateOf(0f) }
    val offsetToActivate = 150f
    val menuWidth = 76.dp

    var countMenuOpened by remember { mutableStateOf(false) }
    var deleteMenuOpened by remember { mutableStateOf(false) }

    Row(
        Modifier
            .height(120.dp)
    ) {
        AnimatedVisibility(countMenuOpened) {
            Row(
                Modifier
                    .width(menuWidth)
                    .fillMaxHeight()
                    .padding(end=10.dp),
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        IconButton(
                            onClick = {  },
                            Modifier.size(30.dp)
                        ) {
                            Icon(
                                painterResource(R.drawable.plus),
                                "minus"
                            )
                        }

                        Text(
                            text = "1",
                            color = Palette.Surface
                        )

                        IconButton(
                            onClick = {  },
                            Modifier.size(30.dp)
                        ) {
                            Icon(
                                painterResource(R.drawable.minus),
                                "minus"
                            )
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            offset = 0f
                        },
                        onDragEnd = {
                            if (countMenuOpened && offset < offsetToActivate) {
                                countMenuOpened = false
                            } else if (deleteMenuOpened && offset > -offsetToActivate) {
                                deleteMenuOpened = false
                            } else {
                                if (offset > offsetToActivate) {
                                    countMenuOpened = true
                                } else if (offset < -offsetToActivate) {
                                    deleteMenuOpened = true
                                }
                            }
                        }
                    ) { _, dragAmount ->
                        offset += dragAmount
                    }
                },
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .padding(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    val context = LocalContext.current
                    val imageRequest = getProductImageRequest(context, product)

                    AsyncImage(
                        imageRequest,
                        "product",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 10.dp)
                    )
                }

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp, horizontal = 10.dp)
                ) {
                    Text(product.name)
                    Text("₽${product.price}", fontSize = 12.sp)
                }
            }
        }

        AnimatedVisibility(deleteMenuOpened) {
            Row(
                Modifier
                    .width(menuWidth)
                    .fillMaxHeight()
                    .padding(start = 10.dp),
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onDelete()
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Palette.Red
                    )
                ) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painterResource(R.drawable.delete),
                            "delete"
                        )
                    }
                }
            }
        }
    }
}