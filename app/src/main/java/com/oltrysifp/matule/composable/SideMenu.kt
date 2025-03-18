package com.oltrysifp.matule.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.SideMenuButton

@Composable
fun SideMenuContent(
    innerPaddingValues: PaddingValues,
    onExit: () -> Unit
) {
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        Column {
            Column(
                Modifier.padding(30.dp)
            ) {
                Image(
                    painterResource(R.drawable.profile_image),
                    "profile_image",
                    Modifier.size(100.dp)
                )

                Spacer(Modifier.padding(4.dp))

                Text(
                    "Эмануэль Кверти",
                    color = Palette.Surface,
                    fontSize = 20.sp
                )
            }

            SideMenuButton(
                painterResource(R.drawable.ic_profile),
                "Профиль"
            ) {}
            SideMenuButton(
                painterResource(R.drawable.bag_white),
                "Корзина"
            ) {}
            SideMenuButton(
                painterResource(R.drawable.ic_heart),
                "Избранное"
            ) {}
            SideMenuButton(
                painterResource(R.drawable.delivery),
                "Заказы"
            ) {}
            SideMenuButton(
                painterResource(R.drawable.ic_ring),
                "Уведомления"
            ) {}
            SideMenuButton(
                painterResource(R.drawable.settings),
                "Настройки"
            ) {}

            Spacer(Modifier.padding(12.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Palette.Surface
            )

            Spacer(Modifier.padding(8.dp))

            SideMenuButton(
                painterResource(R.drawable.exit),
                "Выйти"
            ) { onExit() }
        }
    }
}