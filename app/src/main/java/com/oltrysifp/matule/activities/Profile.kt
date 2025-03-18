package com.oltrysifp.matule.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.util.MyAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@AndroidEntryPoint
class Profile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatuleTheme {
                val sideMenuOpenedDecoy = remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        ProfileScreen(sideMenuOpenedDecoy)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    sideMenuOpened: MutableState<Boolean>,
    viewModel: MyAppViewModel = viewModel()
) {
    val firstName = remember { mutableStateOf("Эмануэль") }
    val lastName = remember { mutableStateOf("Кверти") }
    val address = remember { mutableStateOf("Нигерия") }
    val phoneNumber = remember { mutableStateOf("+7235325332") }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            ProfileHeader(
                "Профиль",
                sideMenuOpened
            )
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Image(
                    painterResource(R.drawable.profile_image),
                    "profile_image",
                    modifier = Modifier.size(96.dp)
                )
                Spacer(Modifier.padding(2.dp))
                Text("Эмануэль Кверти", fontSize = 20.sp)
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(6.dp)
                ) {
                    Text(
                        "Открыть",
                        Modifier
                            .vertical()
                            .rotate(-90f)
                            .drawBehind {
                            }
                    )

                    Image(
                        painterResource(R.drawable.barcode),
                        "barcode",
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }

            Column(
                Modifier.padding(horizontal = 20.dp)
            ) {
                LabeledField("Имя", firstName)
                LabeledField("Фамилия", lastName)
                LabeledField("Адрес", address)
                LabeledField("Телефон", phoneNumber)
            }

            Spacer(Modifier.padding(70.dp))
        }
    }
}

@Composable
fun LabeledField(
    label: String,
    state: MutableState<String>
) {
    Column(
        Modifier.padding(
            top = 16.dp
        )
    ) {
        Text(label,
            fontSize = 18.sp,
            modifier = Modifier.padding(
                bottom = 16.dp
            )
        )
        TextField(
            state.value,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp)),
            onValueChange = {
                state.value = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            colors = Palette.textFieldColors()
        )
    }
}

fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

@Composable
fun ProfileHeader(
    title: String,
    sideMenuOpened: MutableState<Boolean>
) {
    Column {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(
                horizontal = 10.dp
            )
        ) {
            Text(
                title,
                fontSize = 18.sp
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SideMenuOpener {
                    sideMenuOpened.value = true
                }

                Box(
                    Modifier.padding(end=18.dp)
                ) {
                    IconButton(
                        onClick = {

                        },
                        modifier = Modifier
                            .size(30.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_edit),
                            "edit",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
        }
    }
}