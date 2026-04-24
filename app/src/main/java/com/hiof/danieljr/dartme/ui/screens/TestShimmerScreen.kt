package com.hiof.danieljr.dartme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hiof.danieljr.dartme.utility.shimmerLoading
import kotlinx.coroutines.delay

@Composable
fun TestShimmerScreen() {
    var isLoading by remember { mutableStateOf(true) }

    // Automatically stop loading after 3 seconds for demo purposes
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(10000)
            isLoading = false
        }
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = { isLoading = !isLoading },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(if (isLoading) "Stop Loading" else "Simulate Loading")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Profile Feed",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            
            items(6) {
                ShimmerItemCard(isLoading = isLoading)
            }
        }
    }
}

@Composable
fun ShimmerItemCard(isLoading: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image Placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .shimmerLoading(isLoading)
                    .background(if (isLoading) Color.Transparent else Color.LightGray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Name Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerLoading(isLoading)
                        .background(if (isLoading) Color.Transparent else Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerLoading(isLoading)
                        .background(if (isLoading) Color.Transparent else Color.LightGray)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestShimmerScreenPreview() {
    TestShimmerScreen()
}
