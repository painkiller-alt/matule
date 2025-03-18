package com.oltrysifp.matule.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.oltrysifp.matule.Palette

@Composable
fun DefaultLoader(
    maxSizeHorizontally: Boolean = true,
    maxSizeVertically: Boolean = true
) {
    val modifier = if (maxSizeVertically && maxSizeHorizontally) {
        Modifier.fillMaxSize()
    } else if (maxSizeVertically) {
        Modifier.fillMaxHeight()
    } else if (maxSizeHorizontally) {
        Modifier.fillMaxWidth()
    } else {
        Modifier
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Palette.Primary,
            strokeWidth = 3.dp,
            modifier = Modifier.size(40.dp)
        )
    }
}