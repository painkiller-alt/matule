package com.oltrysifp.matule.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.composable.ButtonDefault
import com.oltrysifp.matule.models.User
import com.oltrysifp.matule.util.MyAppViewModel
import com.oltrysifp.matule.util.getSupabaseInstance
import com.oltrysifp.matule.util.log
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.exception.AuthRestException
import io.github.jan.supabase.gotrue.exception.AuthWeakPasswordException
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatuleTheme {
                val mContext = LocalContext.current
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(innerPadding)
                    ) {
                        RegisterScreen(
                            onSignUp = {
                                val intent = Intent(mContext, Greeting::class.java)

                                this@Register.finish()
                                mContext.startActivity(intent)
                            },
                            onLogin = {
                                val intent = Intent(mContext, Login::class.java)

                                this@Register.finish()
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
fun RegisterScreen(
    onSignUp: () -> Unit,
    onLogin: () -> Unit,
    viewModel: MyAppViewModel = viewModel()
) {
    val mContext = LocalContext.current
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val supabase = viewModel.supabase

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
            Text(
                "Регистрация",
                fontWeight = FontWeight.Medium,
                fontSize = 38.sp
            )
            Spacer(Modifier.padding(8.dp))
            Text(
                "Заполните Свои Данные Или Продолжите Через Социальные Медиа",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(Modifier.padding(20.dp))

            Column {
                Text("Ваше Имя")
                TextField(
                    name.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp)),
                    onValueChange = {
                        name.value = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    placeholder = { Text("xxxxxxxxxxx", color = Color.Gray) },
                    colors = Palette.textFieldColors()
                )
            }

            Spacer(Modifier.padding(12.dp))

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.shield),
                    "shield",
                    Modifier.size(12.dp)
                )

                Spacer(Modifier.padding(4.dp))

                Text(
                    "Даю согласие на обработку персональных данных",
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(),
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(Modifier.padding(6.dp))

            val signInCoroutine = rememberCoroutineScope()
            ButtonDefault(
                colors = Palette.buttonPrimaryColors(),
                onClick = {
                    signInCoroutine.launch {
                        if (supabase != null) {
                            try {
                                supabase.auth.signUpWith(Email) {
                                    this.email = email.value
                                    this.password = password.value
                                }

                                val userEmail = supabase.auth.retrieveUserForCurrentSession().email
                                val userUUID = UUID.nameUUIDFromBytes(supabase.auth.retrieveUserForCurrentSession().id.toByteArray())
                                log(userUUID)

                                val userRetrieved = try {
                                    supabase.from("users").select {
                                        filter {
                                            eq("uid", userUUID)
                                        }
                                    }.decodeSingle<User>()
                                } catch (e: Exception) {
                                    null
                                }

                                if (userRetrieved == null) {
                                    userEmail?.let {
                                        supabase.from("users").insert(
                                            User(email = userEmail, phoneNumber = null, firstName = null, lastName = null, address = null, uid = userUUID)
                                        )
                                    }
                                }

                                onSignUp()
                            } catch (e: AuthWeakPasswordException) {
                                log("AuthWeakPasswordException")
                            } catch (e: AuthRestException) {
                                log("AuthRestException: Unable to validate email address: invalid format")
                                // already exists here also
                            }
                        }
                    }
                }
            ) {
                Text(
                    "Зарегестрироваться",
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
                    "Есть Аккаунт?",
                    color = Color.Gray,
                    modifier = Modifier,
                    textAlign = TextAlign.End
                )

                Spacer(Modifier.padding(2.dp))

                Text(
                    "Войти",
                    textAlign = TextAlign.End,
                    modifier = Modifier.clickable {
                        onLogin()
                    }
                )
            }
        }
    }
}