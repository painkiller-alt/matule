package com.oltrysifp.matule.composable

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.Cart
import com.oltrysifp.matule.activities.Details
import com.oltrysifp.matule.getProductImageRequest
import com.oltrysifp.matule.models.Product
import com.oltrysifp.matule.util.log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Dispatcher

@Composable
fun ProductCard(
    product: Product,
    onCart: () -> Unit,
    onFavorite: (Boolean) -> Unit,
    isInCart: MutableState<Boolean>,
    isFavourite: MutableState<Boolean>
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val context = LocalContext.current

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.clickable {
                val intent = Intent(context, Details::class.java)

                val b = Bundle()
                b.putString("product", Json.encodeToString(product))
                intent.putExtras(b)

                context.startActivity(intent)
            }.fillMaxSize()
        ) {
            Box(
                Modifier.fillMaxSize()
            ) {
                Column(
                    Modifier.fillMaxHeight()
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.55f)
                    ) {
                        IconButton(
                            onClick = {
                                isFavourite.value = !isFavourite.value
                                onFavorite(isFavourite.value)
                            }
                        ) {
                            Image(
                                painterResource(
                                    if (!isFavourite.value) R.drawable.heart
                                    else R.drawable.heart_red
                                ),
                                "heart",
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(36.dp)
                            )
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val imageRequest = getProductImageRequest(context, product)

                            AsyncImage(
                                imageRequest,
                                "product",
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .padding(vertical = 10.dp)
                            )
                        }
                    }

                    Text(
                        "BEST SELLER",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(4.dp)
                    )

                    Text(
                        product.name,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                    )

                    Spacer(Modifier.padding(2.dp))

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text("₽", fontSize = 16.sp)
                        Text(
                            product.price.toString(),
                            fontSize = 12.sp
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.BottomEnd)
                        .offset(
                            x = 20.dp,
                            y = 20.dp
                        )
                        .clickable {
                            if (isInCart.value) {
                                openCart(context)
                            } else {
                                isInCart.value = true
                                onCart()
                            }
                        }
                ) {
                    Image(
                        if (!isInCart.value) {
                            painterResource(R.drawable.plus)
                        } else painterResource(R.drawable.cart),
                        "plus",
                        modifier = Modifier
                            .size(19.dp)
                            .offset(
                                x = 10.dp,
                                y = 10.dp
                            )
                    )
                }
            }
        }
    }
}

fun openCart(
    context: Context
) {
    val intent = Intent(context, Cart::class.java)
    context.startActivity(intent)
}