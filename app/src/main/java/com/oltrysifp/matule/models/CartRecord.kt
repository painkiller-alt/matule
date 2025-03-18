package com.oltrysifp.matule.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CartRecord (
    @Serializable(with = UUIDSerializer::class)
    @SerialName("user_id")
    val userId: UUID,
    @SerialName("product_id")
    val productId: Int,
    val count: Int,
    val id: Int? = null
)