package com.oltrysifp.matule.composable

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable

fun toastShort(context: Context, text: String) {
    Toast.makeText(
        context, //Context
        text, // Message to display
        Toast.LENGTH_SHORT // Duration of the message, another possible value is Toast.LENGTH_LONG
    ).show() //Finally Show the toast
}