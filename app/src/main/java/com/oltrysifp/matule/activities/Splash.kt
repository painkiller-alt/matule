package com.oltrysifp.matule.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.composable.EdgeToEdgeConfig
import com.oltrysifp.matule.util.MyAppViewModel
import com.oltrysifp.matule.util.getSupabaseInstance
import com.oltrysifp.matule.util.log
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.delay

@AndroidEntryPoint
class Splash : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mContext = LocalContext.current
            EdgeToEdgeConfig(this)
            MatuleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        Modifier.padding(innerPadding)
                    ) { }

                    SplashScreen(this)
                }
            }
        }
    }
}


@Composable
fun SplashScreen(
    activity: Activity,
    viewModel: MyAppViewModel = viewModel()
) {
    val context = LocalContext.current

    val supabase = viewModel.supabase

    LaunchedEffect(Unit) {
        if (supabase != null) {
            authStateHandler(
                supabase,
                onSession = {
                    val intent = Intent(context, MainMenu::class.java)

                    activity.finish()
                    context.startActivity(intent)
                },
                onSignOut = {
                    val intent = Intent(context, Login::class.java)

                    activity.finish()
                    context.startActivity(intent)
                },
                onAnon = {
                    val intent = Intent(context, Login::class.java)

                    activity.finish()
                    context.startActivity(intent)
                }
            )
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(R.drawable.logo),
            "logo",
            Modifier.size(129.dp)
        )
    }
}