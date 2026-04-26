package com.hiof.danieljr.dartme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiof.danieljr.dartme.logic.ScoreCalculator
import com.hiof.danieljr.dartme.utility.shimmerLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DartGameScreen(playerNames: List<String>, startScore: Int, onBack: () -> Unit) {
    val scores = remember { mutableStateListOf<Int>().apply { addAll(List(playerNames.size) { startScore }) } }
    var currentPlayerIndex by remember { mutableIntStateOf(0) }
    
    val dartInputs = remember { mutableStateListOf("", "", "") }
    var winnerName by remember { mutableStateOf<String?>(null) }
    var showCancelDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val playerColors = listOf(
        Color(0xFF9C27B0), Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFF9800),
        Color(0xFFE91E63), Color(0xFF00BCD4), Color(0xFF8BC34A), Color(0xFFFFC107)
    )

    val currentPlayerName = playerNames[currentPlayerIndex]
    val currentPlayerColor = playerColors[currentPlayerIndex % playerColors.size]
    val currentTotalScore = scores[currentPlayerIndex]
    
    val nextPlayerIndex = (currentPlayerIndex + 1) % playerNames.size
    val nextPlayerName = playerNames[nextPlayerIndex]
    
    val turnSum = dartInputs.sumOf { it.toIntOrNull() ?: 0 }

    if (winnerName != null) {
        WinnerDialog(name = winnerName!!, onRestart = {
            winnerName = null
            scores.indices.forEach { scores[it] = startScore }
            currentPlayerIndex = 0
            dartInputs.fill("")
        })
    }

    if (showCancelDialog) {
        CancelGameDialog(
            onConfirm = onBack,
            onDismiss = { showCancelDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { showCancelDialog = true }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "MATCH SCORE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 2.sp
            )
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerLoading(
                        isLoading = true,
                        shimmerColor = currentPlayerColor.copy(alpha = 0.6f),
                        baseColor = currentPlayerColor.copy(alpha = 0.2f)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = currentPlayerName.uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentTotalScore.toString(),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Black,
                    fontSize = 100.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Text(
                    text = "NEXT UP: $nextPlayerName",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                dartInputs.forEachIndexed { index, value ->
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("DART ${index + 1}", style = MaterialTheme.typography.labelSmall)
                        
                        var textFieldValue by remember { 
                            mutableStateOf(TextFieldValue(text = value)) 
                        }

                        if (textFieldValue.text != value) {
                            textFieldValue = textFieldValue.copy(text = value)
                        }

                        OutlinedTextField(
                            value = textFieldValue,
                            onValueChange = { newValue ->
                                // Validering: Kun tall, maks 60, ingen minus
                                val filtered = newValue.text.filter { it.isDigit() }
                                val intVal = filtered.toIntOrNull() ?: 0
                                
                                if (filtered.isEmpty() || (intVal <= 60)) {
                                    textFieldValue = newValue.copy(text = filtered)
                                    dartInputs[index] = filtered
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        textFieldValue = textFieldValue.copy(
                                            selection = TextRange(0, textFieldValue.text.length)
                                        )
                                    }
                                },
                            textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("QUICK ACTIONS", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickActionButton("DOUBLE", Modifier.weight(1f)) {
                    val lastIdx = dartInputs.indexOfLast { it.isNotEmpty() }
                    val targetIdx = if (lastIdx == -1) 0 else lastIdx
                    val currentVal = dartInputs[targetIdx].toIntOrNull() ?: 0
                    if (currentVal > 0 && currentVal * 2 <= 60) {
                        dartInputs[targetIdx] = (currentVal * 2).toString()
                    }
                }
                QuickActionButton("TRIPLE", Modifier.weight(1f)) {
                    val lastIdx = dartInputs.indexOfLast { it.isNotEmpty() }
                    val targetIdx = if (lastIdx == -1) 0 else lastIdx
                    val currentVal = dartInputs[targetIdx].toIntOrNull() ?: 0
                    if (currentVal > 0 && currentVal * 3 <= 60) {
                        dartInputs[targetIdx] = (currentVal * 3).toString()
                    }
                }
                
                QuickActionButton("25", Modifier.weight(0.7f), containerColor = Color(0xFF2E7D32)) {
                    val emptyIdx = dartInputs.indexOfFirst { it.isEmpty() }
                    if (emptyIdx != -1) dartInputs[emptyIdx] = "25"
                }
                
                QuickActionButton("BULL", Modifier.weight(0.7f), containerColor = Color(0xFFC62828)) {
                    val emptyIdx = dartInputs.indexOfFirst { it.isEmpty() }
                    if (emptyIdx != -1) dartInputs[emptyIdx] = "50"
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            ScoreBarChart(
                playerNames = playerNames,
                scores = scores,
                startScore = startScore,
                currentPlayerIndex = currentPlayerIndex,
                playerColors = playerColors,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val newScore = ScoreCalculator.calculateNewScore(currentTotalScore, turnSum)
                    scores[currentPlayerIndex] = newScore
                    if (ScoreCalculator.isWin(newScore)) {
                        winnerName = currentPlayerName
                    } else {
                        currentPlayerIndex = (currentPlayerIndex + 1) % playerNames.size
                        dartInputs.fill("")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerLoading(
                        isLoading = true,
                        shimmerColor = currentPlayerColor.copy(alpha = 0.6f),
                        baseColor = currentPlayerColor.copy(alpha = 0.2f)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("CONFIRM TURN", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ScoreBarChart(
    playerNames: List<String>,
    scores: List<Int>,
    startScore: Int,
    currentPlayerIndex: Int,
    playerColors: List<Color>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        playerNames.forEachIndexed { index, name ->
            val score = scores[index]
            val progress = (startScore - score).coerceIn(0, startScore).toFloat() / startScore
            val isActive = index == currentPlayerIndex
            val playerColor = playerColors[index % playerColors.size]
            
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = score.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Normal,
                    color = if (isActive) playerColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((progress * 70).dp.coerceAtLeast(4.dp))
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(
                            if (isActive) playerColor 
                            else playerColor.copy(alpha = 0.3f)
                        )
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = name.take(3).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color = if (isActive) playerColor else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    text: String, 
    modifier: Modifier = Modifier, 
    containerColor: Color? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor ?: MaterialTheme.colorScheme.secondaryContainer,
            contentColor = if (containerColor != null) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}

@Composable
fun WinnerDialog(name: String, onRestart: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("🏆 WINNER!") },
        text = { Text("$name won the game!") },
        confirmButton = {
            Button(onClick = onRestart) { Text("NEW GAME") }
        }
    )
}

@Composable
fun CancelGameDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cancel Game?") },
        text = { Text("Are you sure you want to cancel the game and return to the start screen?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("YES, CANCEL")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CONTINUE PLAYING")
            }
        }
    )
}
