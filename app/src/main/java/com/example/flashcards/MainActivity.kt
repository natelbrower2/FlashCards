package com.example.flashcards

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.ui.theme.FlashCardsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCardsTheme {
                FlashCardApp()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FlashCardApp() {
    TextBoxWithButtons(modifier = Modifier
//        .fillMaxSize()
        .wrapContentSize(Alignment.Center))
}

private fun keepIndexInRange(index: Int, length: Int): Int {
    return if (index >= length) {
        0
    }
    else if (index < 0) {
        length - 1
    }
    else {
        index
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TextBoxWithButtons(modifier: Modifier = Modifier) {
    var index by remember { mutableIntStateOf(0) }
//    val frontTextContent: MutableState<String> = mutableStateOf("Front side of flash card")
    var frontTextContent by remember { mutableStateOf("") }
    var backTextContent by remember { mutableStateOf("") }
//    val flashCardItems = mutableListOf("Front1", "Back1","Front2", "Back2","Front3", "Back3")
    val flashCardItems by remember { mutableStateOf(mutableListOf<String>())}
    Column (
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .padding(vertical = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (flashCardItems.size > 0) { // if flash cards have been made, display the flash cards
            Text(
                text = flashCardItems[index],
                fontSize = 25.sp
            )
        }
        else { // if no flash cards have been made yet, prompt the user to create flash cards
            Text(
                text = "Type in your new flash card below.",
                fontSize = 25.sp
            )
            index = 0 // make sure that the index can't move before flash cards have been added
        }
        Button(onClick = {
            if (index % 2 == 0) { // if the index is even
                index++ // flip to the back of the flash card
            }
            else { // if the index is odd
                index-- // flip to the front of the flash card
            }
            // make sure that the index is in the range of the flash card length
            index = keepIndexInRange(index, flashCardItems.size)
        }) {
            Text("Flip")
        }
        Button(onClick = {
            if (index % 2 == 0) { // if the index is even
                index += 2 // add two to skip over the back side of the flash card
            }
            else { // if the index is odd
                index++ // only add one to go to the next flash card
            }
            // make sure that the index is in the range of the flash card length
            index = keepIndexInRange(index, flashCardItems.size)
        }) {
            Text("Next")
        }
        Button(onClick = {
            index -= if (index % 2 == 0) { // if the index is even
                2 // subtract two to get to the previous flash card
            } else { // if the index is odd
                3 // subtract three to get to the previous flash card
            }
            // make sure that the index is in the range of the flash card length
            index = keepIndexInRange(index, flashCardItems.size)
        }) {
            Text("Back")
        }
        TextField(
            value = frontTextContent,
            onValueChange = { frontTextContent = it },
            label = { Text("Front side of flash card") },
            modifier = modifier
        )
        TextField(
            value = backTextContent,
            onValueChange = { backTextContent = it},
            label = { Text("Back side of flash card") },
            modifier = modifier
        )
        Button(onClick = {
            // add the text from the text boxes to another flash card
            flashCardItems.add(frontTextContent)
            flashCardItems.add(backTextContent)
            // erase the two text boxes
            frontTextContent = ""
            backTextContent = ""
        }) {
            Text("Add new flash card")
        }
    }
}