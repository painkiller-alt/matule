package com.oltrysifp.matule

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object Palette {
    val Red = Color(0xFFF87265)
    val Primary = Color(0xFF48B2E7)
    val Disabled = Color(0xFF2B6B8B)
    val Background = Color(0xFFF7F7F9)
    val Surface = Color(0xFFFFFFFF)
    val Text = Color(0xFF2B2B2B)
    val Hint = Color(0xFF6A6A6A)
    val SubTextLight = Color(0xFFD8D8D8)
    val SubTextDark = Color(0xFF707B81)

    @Composable
    fun buttonPrimaryColors(
        containerColor: Color = Primary,
        contentColor: Color = Surface
    ) = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor
    )

    @Composable
    fun buttonWhiteColors(
        containerColor: Color = Surface,
        contentColor: Color = Text
    ) = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor
    )

    @Composable
    fun textFieldColors(
        textColor: Color = Text,
        hintColor: Color = Hint,
        containerColor: Color = Background,
        disabledContainer: Color = containerColor
    ) = TextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,

        focusedPlaceholderColor = hintColor,
        unfocusedPlaceholderColor = hintColor,

        unfocusedContainerColor = containerColor,
        focusedContainerColor = containerColor,

        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,

        disabledIndicatorColor = Color.Transparent,
        disabledContainerColor = disabledContainer
    )
}