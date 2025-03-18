package com.oltrysifp.matule.composable.navigation

import com.oltrysifp.matule.R

sealed class NavigationItem(var route: String, val icon: Int) {
    data object Home : NavigationItem(
        "home",
        R.drawable.ic_home
    )

    data object Favourite : NavigationItem(
        "favourite",
        R.drawable.ic_heart
    )

    data object Notifications : NavigationItem(
        "notifications",
        R.drawable.ic_ring
    )

    data object Profile : NavigationItem(
        "profile",
        R.drawable.ic_profile
    )
}