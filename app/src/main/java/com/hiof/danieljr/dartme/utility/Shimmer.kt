package com.hiof.danieljr.dartme.utility

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerDemoScreen() {
    var isLoading by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .shimmerLoading(isLoading)
                .background(if (isLoading) Color.Transparent else Color.Gray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        repeat(5) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(vertical = 4.dp)
                    .shimmerLoading(isLoading)
                    .background(if (isLoading) Color.Transparent else Color.Gray)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { isLoading = !isLoading }) {
            Text(if (isLoading) "Stop Loading" else "Start Loading")
        }
    }
}

fun Modifier.shimmerLoading(
    isLoading: Boolean,
    durationMillis: Int = 3000,
    shimmerColor: Color = Color(0xFF9C27B0).copy(alpha = 0.6f), // Purple
    baseColor: Color = Color(0xFF4CAF50).copy(alpha = 0.3f)  // Green
): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis)
        ),
        label = "shimmer"
    )

    if (isLoading) {
        this.background(
            brush = Brush.linearGradient(
                colors = listOf(
                    baseColor,
                    shimmerColor,
                    baseColor,
                ),
                start = Offset(startOffsetX, 0f),
                end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
            )
        ).onGloballyPositioned {
            size = it.size
        }
    } else {
        this
    }
}

@Preview(showBackground = true)
@Composable
fun ShimmerDemoScreenPreview() {
    ShimmerDemoScreen()
}
