package com.oltrysifp.matule.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.composable.ButtonDefault
import com.oltrysifp.matule.composable.toast
import com.oltrysifp.matule.util.MyAppViewModel
import com.oltrysifp.matule.util.getSupabaseInstance
import com.oltrysifp.matule.util.log
import com.oltrysifp.matule.util.startActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.SessionSource
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.exception.AuthRestException
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MatuleTheme {
                val mContext = LocalContext.current

                val supabase = getSupabaseInstance(mContext)

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    Box(
                        Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(innerPadding)
                    ) {
                        LoginScreen(
                            onSignIn = {
                                val intent = Intent(mContext, Greeting::class.java)
                                this@Login.finish()
                                mContext.startActivity(intent)
                            },
                            onRegister = {
                                val intent = Intent(mContext, Register::class.java)
                                this@Login.finish()
                                mContext.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    onSignIn: () -> Unit,
    onRegister: () -> Unit,
    viewModel: MyAppViewModel = viewModel()
) {
    val mContext = LocalContext.current
    val activity = (mContext as Activity)

    val supabase = viewModel.supabase
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        supabase?.auth?.sessionStatus?.collect {
            if (it is SessionStatus.Authenticated && it.source is SessionSource.SignIn) {
                activity.finish()
                startActivity(mContext, MainMenu::class.java)
            } else if (it is SessionStatus.NotAuthenticated && it.isSignOut) {
                toast(mContext, "Неправильные данные")
                return@collect
            }
        }
    }

    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            Modifier.fillMaxHeight(0.15f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    "Привет",
                    fontWeight = FontWeight.Medium,
                    fontSize = 38.sp
                )
                Text(
                    "!",
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    fontSize = 38.sp
                )
            }
            Spacer(Modifier.padding(8.dp))
            Text(
                "Заполните Свои Данные Или Продолжите Через Социальные Медиа",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(Modifier.padding(20.dp))

            Column {
                Text("Email")
                TextField(
                    email.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp)),
                    onValueChange = {
                        email.value = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    placeholder = { Text("xyz@gmail.com", color = Color.Gray) },
                    colors = Palette.textFieldColors()
                )
            }

            Spacer(Modifier.padding(12.dp))

            Column {
                var showPassword by remember { mutableStateOf(false) }

                Text("Пароль")
                TextField(
                    password.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp)),
                    onValueChange = {
                        password.value = it
                    },
                    placeholder = { Text("••••••••", color = Color.Gray) },
                    trailingIcon = {
                        IconButton(
                            onClick = { showPassword = !showPassword }
                        ) {
                            Icon(
                                painterResource(
                                    if (!showPassword) {
                                        R.drawable.ic_hide
                                    } else {
                                        R.drawable.eye_open
                                    }
                                ),
                                "hide or show password"
                            )
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    colors = Palette.textFieldColors()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    "Восстановить",
                    color = Color.Gray,
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(mContext, ForgotPassword::class.java)

                            mContext.startActivity(intent)
                        }
                )
            }

            val loginCoroutine = rememberCoroutineScope()
            ButtonDefault(
                colors = Palette.buttonPrimaryColors(),
                onClick = {
                    loginCoroutine.launch {
                        try {
                            supabase?.auth?.signInWith(Email) {
                                this.email = email.value
                                this.password = password.value
                            }
                        } catch (e: AuthRestException) {
                            toast(mContext, "Неправильные данные")
                            return@launch
                        } catch (e: Exception) {
                            toast(mContext, "Ошибка")
                            return@launch
                        }
                    }
                }
            ) {
                Text(
                    "Войти",
                    color = MaterialTheme.colorScheme.surface
                )
            }

            Row(
                Modifier
                    .fillMaxHeight()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "Вы впервые?",
                    color = Color.Gray,
                    modifier = Modifier,
                    textAlign = TextAlign.End
                )

                Spacer(Modifier.padding(2.dp))

                Text(
                    "Создать пользователя",
                    textAlign = TextAlign.End,
                    modifier = Modifier.clickable {
                        onRegister()
                    }
                )
            }
        }
    }
}

suspend fun authStateHandler(
    supabase: SupabaseClient,
    onSignIn: (SessionStatus) -> Unit = {},
    onSignUp: (SessionStatus) -> Unit = {},
    onSignOut: (SessionStatus) -> Unit = {},

    onSession: (SessionStatus) -> Unit = {},
    onAnon: (SessionStatus) -> Unit = {},
) {
    supabase.auth.sessionStatus.collect {
        when(it) {
            is SessionStatus.Authenticated -> {
                log("Received new authenticated session.")
                onSession(it)
                when(it.source) { //Check the source of the session
                    SessionSource.External -> {log("external")}
                    is SessionSource.Refresh -> {log("Refresh")}
                    is SessionSource.SignIn -> onSignIn(it)
                    is SessionSource.SignUp -> onSignUp(it)
                    SessionSource.Storage -> {log("Storage")}
                    SessionSource.Unknown -> {log("Unknown")}
                    is SessionSource.UserChanged -> {log("UserChanged")}
                    is SessionSource.UserIdentitiesChanged -> {log("UserIdentitiesChanged")}
                    else -> {}
                }
            }
            is SessionStatus.NotAuthenticated -> {
                if(it.isSignOut) {
                    onSignOut(it)
                    log("User signed out")
                } else {
                    onAnon(it)
                    log("User not signed in")
                }
            }
            else -> {}
        }
    }
}