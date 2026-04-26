package com.hiof.danieljr.dartme.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiof.danieljr.dartme.R
import com.hiof.danieljr.dartme.utility.shimmerLoading

@Composable
fun DartInputScreen(onStartGame: (List<String>, Int) -> Unit) {
    var playerCount by remember { mutableIntStateOf(2) }
    // Starter med tomme navn slik at feltene er blanke
    val playerNames = remember { mutableStateListOf("", "") }
    var gameType by remember { mutableIntStateOf(501) }

    // Juster antall spillere i lista når playerCount endres
    LaunchedEffect(playerCount) {
        if (playerNames.size < playerCount) {
            repeat(playerCount - playerNames.size) {
                playerNames.add("")
            }
        } else if (playerNames.size > playerCount) {
            repeat(playerNames.size - playerCount) {
                playerNames.removeAt(playerNames.size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dartmetrans),
                    contentDescription = "DartMe Logo",
                    modifier = Modifier.size(100.dp).padding(bottom = 8.dp)
                )
                Text(
                    text = "GAME SETUP",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black, letterSpacing = 2.sp
                    )
                )
                Box(modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)).shimmerLoading(true))
            }
        },
        bottomBar = {
            Button(
                onClick = { 
                    // Hvis navnet er tomt, bruker vi "Player X" som standard når spillet starter
                    val finalNames = playerNames.mapIndexed { index, name ->
                        name.ifBlank { "Player ${index + 1}" }
                    }
                    onStartGame(finalNames, gameType) 
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp).clip(RoundedCornerShape(12.dp)).shimmerLoading(true),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "START MATCH", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Game Mode", style = MaterialTheme.typography.titleMedium)
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    listOf(301, 501, 701).forEachIndexed { index, type ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = 3),
                            onClick = { gameType = type },
                            selected = gameType == type
                        ) { Text(type.toString()) }
                    }
                }
            }

            item {
                PlayerCountSelector(count = playerCount, onCountChange = { playerCount = it.coerceIn(1, 8) })
            }

            item { Text("Player Names", style = MaterialTheme.typography.titleMedium) }

            itemsIndexed(playerNames) { index, name ->
                PlayerInputCard(index = index, name = name, onNameChange = { playerNames[index] = it })
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun PlayerCountSelector(count: Int, onCountChange: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Number of Players", fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onCountChange(count - 1) }) {
                    Text("-", style = MaterialTheme.typography.headlineMedium)
                }
                
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerLoading(true),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                IconButton(onClick = { onCountChange(count + 1) }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }
        }
    }
}

@Composable
fun PlayerInputCard(index: Int, name: String, onNameChange: (String) -> Unit) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = name)) }

    LaunchedEffect(name) {
        if (name != textFieldValue.text) {
            textFieldValue = textFieldValue.copy(text = name)
        }
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            onNameChange(it.text)
        },
        label = { Text("Player ${index + 1}") },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
        modifier = Modifier.fillMaxWidth().onFocusChanged {
            if (it.isFocused) {
                // Markerer alt hvis det er tekst der, ellers er det alt klart for skriving
                textFieldValue = textFieldValue.copy(selection = TextRange(0, textFieldValue.text.length))
            }
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}
