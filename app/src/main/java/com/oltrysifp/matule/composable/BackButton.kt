package com.oltrysifp.matule.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.oltrysifp.matule.R

@Composable
fun BackButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.padding(20.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(42.dp)
        ) {
            Icon(
                painterResource(R.drawable.left_arrow),
                "left_arrow",
                modifier = Modifier.size(14.dp)
            )
        }
    }
}