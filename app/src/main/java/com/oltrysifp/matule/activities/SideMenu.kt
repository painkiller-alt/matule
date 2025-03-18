package com.oltrysifp.matule.activities

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun SideMenu(
    drawContent: @Composable () -> Unit,
    isOpened: MutableState<Boolean>,
    content: @Composable () -> Unit
) {
    val size = animateFloatAsState(if (isOpened.value) 0.7f else 1.0f, label = "size")
    val trY = animateFloatAsState(if (isOpened.value) 2.4f else 0.0f, label = "try")
    val trX = animateFloatAsState(if (isOpened.value) 0.5f else 0.0f, label = "trx")
    val rotation = animateFloatAsState(if (isOpened.value) -3f else 0.0f, label = "rotation")

    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            content()
        }

            Box(
                modifier = Modifier
                    .graphicsLayer
                    {
                        scaleX = size.value
                        scaleY = size.value

                        transformOrigin = TransformOrigin(trY.value, trX.value)
                        rotationZ = rotation.value
                        shape = RoundedCornerShape(if (isOpened.value) 32.dp else 0.dp)
                    }
                    .clip(RoundedCornerShape(if (isOpened.value) 32.dp else 0.dp))
            ) {
                drawContent()
                if (isOpened.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { isOpened.value = false }
                    )
                }
            }
    }
}