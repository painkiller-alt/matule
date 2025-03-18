package com.oltrysifp.matule.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.ui.theme.MatuleTheme
import com.oltrysifp.matule.composable.ButtonDefault
import com.oltrysifp.matule.composable.EdgeToEdgeConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Greeting : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EdgeToEdgeConfig(this)
            MatuleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingScreen(innerPadding)
                }
            }
        }
    }
}

@Composable
fun GreetingScreen(
    innerPaddingValues: PaddingValues
) {
    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )
    val page = remember { mutableIntStateOf(0) }

    LaunchedEffect( pagerState.currentPage ) {
        page.intValue = pagerState.currentPage
    }

    Column {
        Box {
            val brush = Brush.verticalGradient(listOf(
                MaterialTheme.colorScheme.primary,
                Palette.Disabled
            ))
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red),
                onDraw = {
                    drawRect(
                        brush
                    )
                }
            )

            AnimatedContent(
                pagerState.currentPage,
                label="image fade",
                transitionSpec = {
                    fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                }
            ) { page ->
                when (page) {
                    0 -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(
                                painterResource(R.drawable.greeting_page1),
                                "Изображение фона"
                            )
                        }
                    }

                    1 -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(
                                painterResource(R.drawable.greeting_page2),
                                "Изображение фона",
                                modifier = Modifier.offset(y= (-200).dp)
                            )
                        }
                    }

                    2 -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(
                                painterResource(R.drawable.greeting_page3),
                                "Изображение фона",
                                modifier = Modifier.offset(y= (-200).dp)
                            )
                        }
                    }
                }
            }

            Box(
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y=650.dp)
                ) {
                    val animatedWidth1 by animateDpAsState(
                        targetValue = if (page.intValue == 0) 40.dp else 30.dp,
                        label = "animatedWidth1"
                    )
                    HorizontalDivider(
                        Modifier
                            .width(animatedWidth1)
                            .clip(
                                shape = RoundedCornerShape(50)
                            ),
                        color = if (page.intValue == 0) Color.White else Palette.Disabled,
                        thickness = 4.dp
                    )

                    Spacer(Modifier.padding(5.dp))

                    val animatedWidth2 by animateDpAsState(
                        targetValue = if (page.intValue == 1) 40.dp else 30.dp,
                        label = "animatedWidth1"
                    )
                    HorizontalDivider(
                        Modifier
                            .width(animatedWidth2)
                            .clip(
                                shape = RoundedCornerShape(50)
                            ),
                        color = if (page.intValue == 1) Color.White else Palette.Disabled,
                        thickness = 4.dp
                    )

                    Spacer(Modifier.padding(5.dp))

                    val animatedWidth3 by animateDpAsState(
                        targetValue = if (page.intValue == 2) 40.dp else 30.dp,
                        label = "animatedWidth1"
                    )
                    HorizontalDivider(
                        Modifier
                            .width(animatedWidth3)
                            .clip(
                                shape = RoundedCornerShape(50)
                            ),
                        color = if (page.intValue == 2) Color.White else Palette.Disabled,
                        thickness = 4.dp
                    )
                }
            }

            val scrollScope = rememberCoroutineScope()
            HorizontalPager(
                state = pagerState
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPaddingValues)
                ) {
                    when (page) {
                        0 -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                            ) {
                                Spacer(Modifier.padding(20.dp))
                                Text("ДОБРО", fontSize = 30.sp, color = Color.White)
                                Text("ПОЖАЛОВАТЬ", fontSize = 30.sp, color = Color.White)
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(vertical = 40.dp, horizontal = 20.dp),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                ButtonDefault(
                                    colors = Palette.buttonWhiteColors(),
                                    onClick = {
                                        scrollScope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage+1)
                                        }
                                    }
                                ) {
                                    Text("Начать")
                                }
                            }
                        }

                        1 -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .offset(y=80.dp)
                            ) {
                                Text("Начнем", fontSize = 30.sp, color = Color.White)
                                Text("путешествие", fontSize = 30.sp, color = Color.White)

                                Spacer(Modifier.padding(10.dp))

                                Text(
                                    modifier = Modifier.fillMaxWidth(0.8f),
                                    text="Умная, великолепная и модная коллекция Изучите сейчас",
                                    textAlign = TextAlign.Center,
                                    color = Palette.SubTextLight
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(vertical = 40.dp, horizontal = 20.dp),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                ButtonDefault(
                                    colors = Palette.buttonWhiteColors(),
                                    onClick = {
                                        scrollScope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage+1)
                                        }
                                    }
                                ) {
                                    Text("Далее")
                                }
                            }
                        }

                        2 -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .offset(y=80.dp)
                            ) {
                                Text("У Вас Есть Сила,", fontSize = 30.sp, color = Color.White)
                                Text("Чтобы", fontSize = 30.sp, color = Color.White)

                                Spacer(Modifier.padding(10.dp))

                                Text(
                                    modifier = Modifier.fillMaxWidth(0.8f),
                                    text="В вашей комнате много красивых и привлекательных растений",
                                    textAlign = TextAlign.Center,
                                    color = Palette.SubTextLight
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(vertical = 40.dp, horizontal = 20.dp),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                val context = LocalContext.current
                                ButtonDefault(
                                    colors = Palette.buttonWhiteColors(),
                                    onClick = {
                                        val intent = Intent(context, MainMenu::class.java)

                                        context.startActivity(intent)
                                    }
                                ) {
                                    Text("Далее")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}