package com.oltrysifp.matule.composable.navigation

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.oltrysifp.matule.Palette
import com.oltrysifp.matule.R
import com.oltrysifp.matule.activities.Cart
import com.oltrysifp.matule.composable.ButtonDefault

@Composable
fun NavBar(
    navController: NavController,
    routes: List<NavigationItem>,
    selectedItem: MutableIntState,
    currentRoute: MutableState<String>
) {
    fun navigateIn(
        index: Int
    ) {
        navigate(
            navController,
            routes,
            selectedItem,
            currentRoute,
            index
        )
    }

    // shadow
    Box(
        modifier = with (Modifier){
            fillMaxSize()

                .blur(10.dp)
                .paint(
                    painterResource(id = R.drawable.navbar),
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.BottomCenter,
                    colorFilter = ColorFilter.tint(
                        Color.Black
                    ),
                    alpha = 0.04f
                )
        }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.navbar),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.BottomCenter
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            Modifier
                .size(58.dp)
                .offset(y= (-50).dp),
            color = Palette.Primary,
            shape = CircleShape,
            shadowElevation = 4.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                val context = LocalContext.current
                Button (
                    onClick = {
                        val intent = Intent(context, Cart::class.java)

                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) { }

                Icon(
                    painterResource(R.drawable.bag_white),
                    "cart",
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        val offset = 118
        val bottomPadding = 10

        Row(
            modifier = Modifier
                .offset(x= (-offset).dp)
                .padding(bottom = bottomPadding.dp)
        ) {
            NavBarIcon(
                routes[0],
                currentRoute,
                onClick = { navigateIn(0) },
            )
            Spacer(Modifier.padding(12.dp))
            NavBarIcon(
                routes[1],
                currentRoute,
                onClick = { navigateIn(1) },
            )
        }

        Row(
            modifier = Modifier
                .offset(x= (offset).dp)
                .padding(bottom = bottomPadding.dp)
        ) {
            NavBarIcon(
                routes[2],
                currentRoute,
                onClick = { navigateIn(2) },
            )
            Spacer(Modifier.padding(12.dp))
            NavBarIcon(
                routes[3],
                currentRoute,
                onClick = { navigateIn(3) },
            )
        }
    }
}

@Composable
fun NavBarIcon(
    item: NavigationItem,
    currentRoute: MutableState<String>,
    onClick: () -> Unit
) {
    IconButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .size(50.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Icon(
            painterResource(item.icon),
            item.route,
            tint =
            if (currentRoute.value == item.route) MaterialTheme.colorScheme.primary
            else Palette.Hint,
            modifier = Modifier.size(26.dp)
        )
    }
}

fun navigate(
    navController: NavController,
    routes: List<NavigationItem>,
    selectedItem: MutableIntState,
    currentRoute: MutableState<String>,
    index: Int
) {
    selectedItem.intValue = index
    val item = routes[index]
    currentRoute.value = item.route
    navController.navigate(item.route) {
        navController.graph.startDestinationRoute?.let { route ->
            popUpTo(route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}