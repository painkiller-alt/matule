package com.oltrysifp.matule.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.oltrysifp.matule.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.delay

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    val result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}

suspend inline fun <T> safeSupabaseOperation(
    supabaseClient: SupabaseClient?,
    onFailure: (Exception) -> T = { throw it },
    maxRetries: Int = 3,
    delayMillis: Long = 1000,
    operation: SupabaseClient.() -> T
): T {
    var currentRetries = 0
    var lastError: Exception? = null

    while (currentRetries < maxRetries && supabaseClient != null) {
        try {
            return supabaseClient.run(operation)
        } catch (e: Exception) {
            lastError = e
            currentRetries++
            log("retry $currentRetries")
            delay(delayMillis)
        }
    }

    return if (lastError != null) {
        onFailure(lastError)
    } else {
        onFailure(IllegalStateException("Network Error"))
    }
}

fun getSupabaseClient(context: Context): SupabaseClient? {
    if (isInternetAvailable(context)) {
        val supabase = createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Auth)
            install(Postgrest)
            install(Storage)
        }
        return supabase
    } else {
        return null
    }
}

fun getSupabaseInstance(
    context: Context
): SupabaseClient? {
    val supabase = (context.applicationContext as MatuleApplication).supabase

    return supabase
}