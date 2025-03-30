package com.oltrysifp.matule.composable

import android.content.Context
import android.widget.Toast

fun toast(context: Context, text: String) {
    Toast.makeText(
        context, //Context
        text, // Message to display
        Toast.LENGTH_LONG // Duration of the message, another possible value is Toast.LENGTH_LONG
    ).show() //Finally Show the toast
}