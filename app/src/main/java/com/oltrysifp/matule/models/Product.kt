package com.oltrysifp.matule.models

import android.graphics.Bitmap
import androidx.compose.ui.graphics.painter.Painter
import kotlinx.serialization.Serializable

@Serializable
data class ProductRecord (
    val name: String,
    val descr: String,
    val price: Float,
    val id: Int? = null
)

@Serializable
data class Product (
    val name: String,
    val descr: String,
    val price: Float,
    val image: String,
    val id: Int? = null
)