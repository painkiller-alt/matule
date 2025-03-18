package com.oltrysifp.matule.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.composable.BackButton
import com.oltrysifp.matule.composable.navigation.NavigationItem
import com.oltrysifp.matule.util.MyAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@AndroidEntryPoint
class Notifications : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatuleTheme {
                val sideMenuOpenedDecoy = remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        Modifier.padding(innerPadding)
                    ) {
                        NotificationsScreen(sideMenuOpenedDecoy)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationsScreen(
    sideMenuOpened: MutableState<Boolean>,
    viewModel: MyAppViewModel = viewModel()
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            NotificationsHeader(
                "Уведомления",
                sideMenuOpened
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(
                horizontal = 18.dp
            )
        ) {
            items(5) {
                Notification(
                    "Заголовок",
                    "Lorem ipsum dolor sit amet consectetur. Venenatis pulvinar lobortis risus consectetur morbi egestas id in bibendum. Volutpat nulla urna sit sed diam nulla.",
                    LocalDateTime.of(LocalDate.now(), LocalTime.now())
                )
            }

            item { Spacer(Modifier.padding(70.dp)) }
        }
    }
}


@Composable
fun NotificationsHeader(
    title: String,
    sideMenuOpened: MutableState<Boolean>
) {
    Column(
        Modifier
            .height(56.dp)
    ) {
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
                    modifier = Modifier.padding(10.dp)
                ) {
                    Spacer(
                        modifier = Modifier.size(42.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Notification(
    title: String,
    body: String,
    timestamp: LocalDateTime
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            Modifier.padding(8.dp)
        ) {
            Text(title, fontSize = 18.sp)
            Spacer(Modifier.padding(6.dp))
            Text(body, fontSize = 13.sp, lineHeight = 14.4.sp)
            Spacer(Modifier.padding(10.dp))

            val dateStr = "${normalizeTime(timestamp.dayOfMonth)}." +
                    "${normalizeTime(timestamp.monthValue+1)}." +
                    "${timestamp.year}"

            val timeStr = "${normalizeTime(timestamp.hour)}:${normalizeTime(timestamp.minute)}"

            Text("$dateStr, $timeStr", color = Palette.Hint)
        }
    }
}

fun normalizeTime(timeDigit: Int): String {
    return if (timeDigit < 10) "0$timeDigit" else timeDigit.toString()
}