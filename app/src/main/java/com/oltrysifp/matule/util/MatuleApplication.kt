package com.oltrysifp.matule.util

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.annotation.ExperimentalCoilApi
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltAndroidApp
class MatuleApplication : Application() {
    val supabase by lazy {
        getSupabaseClient(this)
    }
    val datastore by lazy {
        DataStoreManager(this)
    }
}

class MyAppViewModel(
    application: Application
): AndroidViewModel(application) {
    val supabase: SupabaseClient? by lazy {
        (application as MatuleApplication).supabase
    }
    val datastore: DataStoreManager by lazy {
        (application as MatuleApplication).datastore
    }
}