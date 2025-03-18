package com.oltrysifp.matule.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.composable.ButtonDefault
import com.oltrysifp.matule.util.MyAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@AndroidEntryPoint
class ForgotPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatuleTheme {
                val mContext = LocalContext.current
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val mailSentPopUp = remember { mutableStateOf(false) }

                    MailSentPopUp(mailSentPopUp) {
                        mailSentPopUp.value = false
                    }

                    val animatedBlur by animateDpAsState(
                        targetValue = if (mailSentPopUp.value) 6.dp else 0.dp,
                        label = "blur"
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(innerPadding)
                            .blur(animatedBlur)
                    ) {
                        ForgotPasswordScreen {
                            val intent = Intent(mContext, OTPCheck::class.java)

                            this@ForgotPassword.finish()
                            mContext.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    viewModel: MyAppViewModel = viewModel(),
    onOTP: () -> Unit
) {
    val email = remember { mutableStateOf("") }

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
                    "Забыл Пароль",
                    fontWeight = FontWeight.Medium,
                    fontSize = 38.sp
                )
            }
            Spacer(Modifier.padding(8.dp))
            Text(
                "Введите свою учетную запись\n" +
                        " для сброса",
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

            ButtonDefault(
                colors = Palette.buttonPrimaryColors(),
                onClick = {
                    onOTP()
                }
            ) {
                Text(
                    "Отправить",
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

@Composable
fun MailSentPopUp(
    mailSentPopUp: MutableState<Boolean>,
    onDismiss: () -> Unit
) {
    when {
        mailSentPopUp.value -> {
            Dialog(onDismissRequest = { onDismiss() }) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(26.dp)
                    ) {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .padding(top = 2.dp, bottom = 18.dp)
                                .size(56.dp),
                        ) {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painterResource(R.drawable.email),
                                    contentDescription = "Email",
                                    modifier = Modifier
                                        .size(30.dp),
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            }
                        }
                        Text(text = "Проверьте Ваш Email", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.padding(2.dp))
                        Text(
                            text = "Мы отправили код восстановления пароля на вашу электронную почту.",
                            color = Palette.Hint,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}