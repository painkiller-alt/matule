package com.oltrysifp.matule

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.oltrysifp.matule.models.Product

fun getProductImageRequest(
    context: Context,
    product: Product
): ImageRequest {
    val imageRequest = ImageRequest.Builder(context)
        .data(product.image)
        .memoryCacheKey(product.name)
        .diskCacheKey(product.name)
        .error(R.drawable.product)
        .fallback(R.drawable.product)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    return imageRequest
}