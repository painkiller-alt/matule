package com.oltrysifp.matule.util

import android.content.Context
import android.content.Intent
import android.os.Bundle

fun startActivity(
    context: Context,
    activity: Class<*>,
    b: Bundle? = null
) {
    val intent = Intent(context, activity)
    if (b != null) {
        intent.putExtras(b)
    }
    context.startActivity(intent)
}