package com.oltrysifp.matule.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.composable.ButtonDefault
import com.oltrysifp.matule.composable.Header
import com.oltrysifp.matule.cutString
import com.oltrysifp.matule.data.LoadProducts
import com.oltrysifp.matule.getProductImageRequest
import com.oltrysifp.matule.models.Product
import com.oltrysifp.matule.util.MyAppViewModel
import com.oltrysifp.matule.util.getSupabaseInstance
import com.oltrysifp.matule.util.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class Details : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val b = intent.extras ?: return

        val productStr = b.getString("product").toString()
        val product = Json.decodeFromString<Product>(productStr)

        log(product)

        setContent {
            MatuleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        Modifier.padding(innerPadding)
                    ) {
                        DetailsScreen(product) { this@Details.finish() }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsScreen(
    product: Product,
    viewModel: MyAppViewModel = viewModel(),
    onExit: () -> Unit
) {
    val maxChar = 92
    val descrCollapsed = remember { mutableStateOf(true) }
    val supabase = viewModel.supabase

    val alternativeProducts = remember { mutableStateListOf(product) }
    LoadProducts(supabase, alternativeProducts)

    val selectedProductIndex = remember { mutableIntStateOf(0) }
    var selectedProduct by remember { mutableStateOf(alternativeProducts[selectedProductIndex.intValue]) }

    LaunchedEffect(selectedProductIndex.intValue) {
        selectedProduct = alternativeProducts[selectedProductIndex.intValue]
    }

    Column {
        Header("Sneaker Shop", { onExit() }) {
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
        }

        Column(
            Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                selectedProduct.name,
                fontSize = 32.sp,
                lineHeight = 34.sp
            )
            Spacer(Modifier.padding(4.dp))
            Text("Men's Shoes", color = Palette.SubTextDark, fontSize = 16.sp)
            Spacer(Modifier.padding(7.dp))
            Text("₽${selectedProduct.price}", fontSize = 20.sp)

            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(R.drawable.shadow),
                    "shadow",
                    modifier = Modifier
                        .offset(y = 40.dp, x = 40.dp)
                        .fillMaxWidth()
                )

                Image(
                    painterResource(R.drawable.platform),
                    "shadow",
                    modifier = Modifier
                        .offset(y = 65.dp)
                        .fillMaxWidth()
                )

                val context = LocalContext.current
                val imageRequest = getProductImageRequest(context, selectedProduct)

                AsyncImage(
                    imageRequest,
                    "shoe image",
                    modifier = Modifier
                        .size(220.dp)
                )
            }

            Spacer(Modifier.padding(20.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(alternativeProducts) { product ->
                    Card(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                selectedProduct = product
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = if (selectedProduct == product) { BorderStroke(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        ) } else null
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val context = LocalContext.current
                            val imageRequest = getProductImageRequest(context, product)

                            AsyncImage(
                                imageRequest,
                                "product preview"
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.padding(20.dp))

            if (descrCollapsed.value) {
                Text(
                    cutString(selectedProduct.descr, maxChar),
                    color = Palette.Hint,
                )
                if (selectedProduct.descr.length > maxChar) {
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Подробнее",
                            color=MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                descrCollapsed.value = false
                            }
                        )
                    }
                }
            } else {
                Column(
                    Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(
                        selectedProduct.descr,
                        color = Palette.Hint,
                    )
                }
                if (selectedProduct.descr.length > maxChar) {
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Свернуть",
                            color=MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                descrCollapsed.value = true
                            }
                        )
                    }
                }
            }

            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    Box(
                        Modifier.padding(10.dp)
                    ) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(42.dp)
                        ) {
                            Image(
                                painterResource(R.drawable.heart),
                                "heart",
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    }

                    ButtonDefault(
                        colors = Palette.buttonPrimaryColors(),
                        onClick = {

                        }
                    ) {
                        Box(
                            Modifier.fillMaxSize()
                        ) {
                            Image(
                                painterResource(R.drawable.bag_white),
                                "bag",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 14.dp)
                                    .size(28.dp)
                            )
                            Text(
                                "В Корзину",
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}