package com.oltrysifp.matule.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ButtonDefault(
    colors: ButtonColors,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Button(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        onClick = onClick,
        colors = colors,
        contentPadding = PaddingValues(0.dp)
    ) {
        content()
    }
}