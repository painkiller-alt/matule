package com.oltrysifp.matule.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartRecord (
    @SerialName("user_email")
    val userEmail: String,
    @SerialName("product_id")
    val productId: Int,
    val count: Int,
    val id: Int? = null
)