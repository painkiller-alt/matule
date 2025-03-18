package com.oltrysifp.matule.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.util.MyAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class OTPCheck : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatuleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(innerPadding)
                    ) {
                        OTPCheckScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun OTPCheckScreen(
    viewModel: MyAppViewModel = viewModel()
) {
    var timeLeft by remember { mutableIntStateOf(60) }
    val otpCode = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            timeLeft--
            delay(1000)
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(
            Modifier.fillMaxHeight(0.15f)
        )

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "OTP Проверка",
                fontWeight = FontWeight.Medium,
                fontSize = 38.sp
            )
            Spacer(Modifier.padding(8.dp))
            Text(
                "Пожалуйста, проверьте свою электронную почту, чтобы увидеть код подтверждения",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }

        Spacer(Modifier.padding(10.dp))

        Text("OTP Код", fontSize = 21.sp)

        Spacer(Modifier.padding(6.dp))

        BasicTextField(
            otpCode.value,
            onValueChange = {
                if (it.length <= 6) {
                    otpCode.value = it
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            decorationBox = {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(6) { index ->
                        val char = when {
                            index >= otpCode.value.length -> ""
                            else -> otpCode.value[index].toString()
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                if (otpCode.value.length == index) Palette.SubTextLight
                                else Palette.Background
                            ),
                            border = if (char.isEmpty()) BorderStroke(
                                width = 2.dp,
                                MaterialTheme.colorScheme.error
                            ) else BorderStroke(width = 0.dp, color = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(100.dp)
                        ) {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = char, fontSize = 36.sp)
                            }
                        }
                    }
                }
            }
        )

        Spacer(Modifier.padding(10.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Отправить заново", color = if (timeLeft > 0) Palette.SubTextLight else Palette.SubTextDark)
            val textZero = if (timeLeft < 10) "0" else ""
            Text(
                if (timeLeft > 0) "00:$textZero$timeLeft" else "",
                color = Palette.Hint
            )
        }
    }
}