package com.oltrysifp.matule.composable

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    title: String,
    onExit: () -> Unit,
    trailingButton: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BackButton { onExit() }

        Text(
            title,
            fontSize = 18.sp
        )

        Box(
            modifier = Modifier.padding(20.dp)
        ) {
            Spacer(
                modifier = Modifier.size(42.dp)
            )

            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(42.dp)
            ) {
                trailingButton()
            }
        }
    }
}