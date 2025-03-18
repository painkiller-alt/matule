package com.oltrysifp.matule.activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.composable.Header
import com.oltrysifp.matule.util.MyAppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Search : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatuleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        Modifier.padding(innerPadding)
                    ) {
                        SearchScreen { this@Search.finish() }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreen(
    viewModel: MyAppViewModel = viewModel(),
    onExit: () -> Unit
) {
    val queue = remember { mutableStateOf("") }

    Column {
        Header("Поиск", { onExit() }) { }

        Column(
            Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            SearchWithSpeak(queue)

            Spacer(Modifier.padding(6.dp))

            LazyColumn {
               item { RecentSearch("New Shoes") }
               item { RecentSearch("Nike Top Shoes") }
               item { RecentSearch("New Shoes") }
               item { RecentSearch("Nike Top Shoes") }
               item { RecentSearch("New Shoes") }
               item { RecentSearch("Nike Top Shoes") }
            }
        }
    }
}

@Composable
fun SearchWithSpeak(queue: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Surface(
            shadowElevation = 1.dp, // play with the elevation values,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(0.8f),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    queue.value,
                    onValueChange = {
                        queue.value = it },
                    placeholder = { Text("Поиск", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.search),
                            "search",
                            modifier = Modifier.size(18.dp)
                        ) },
                    colors = Palette.textFieldColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.weight(1f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VerticalDivider(
                        thickness = 1.5.dp,
                        color = Palette.SubTextLight,
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .clip(RoundedCornerShape(50))
                    )

                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier.padding(start = 2.dp, end = 8.dp)
                    ) {
                        IconButton(
                            onClick = {  },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Image(
                                painterResource(R.drawable.speak),
                                "filters",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecentSearch(queue: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(R.drawable.recent),
            "recent",
            modifier = Modifier
                .padding(vertical = 10.dp)
                .size(22.dp),
            tint = Palette.SubTextDark
        )

        Spacer(Modifier.padding(6.dp))

        Text(queue, fontSize = 17.sp)
    }
}