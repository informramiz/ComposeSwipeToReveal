@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.example.composeswipetoreveal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.composeswipetoreveal.ui.screens.MainItem
import com.example.composeswipetoreveal.ui.screens.MainScreen
import com.example.composeswipetoreveal.ui.theme.ComposeSwipeToRevealTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSwipeToRevealTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}


@Composable
private fun AndroidExample(items: List<MainItem>, onItemDismiss: (MainItem) -> Unit) {
    val updatedOnDismissItem by rememberUpdatedState(newValue = onItemDismiss)
    LazyColumn {
        items(items = items, key = { it.id }) { item ->
            val updatedItem by rememberUpdatedState(newValue = item)
            var unread by remember { mutableStateOf(false) }
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if (it == DismissValue.DismissedToEnd) unread = !unread
                    if (it == DismissValue.DismissedToStart) {
                        updatedOnDismissItem(updatedItem)
                    }
                    it != DismissValue.DismissedToEnd
                }
            )
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = 4.dp),
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                background = {
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                    val color by animateColorAsState(
                        targetValue = when (dismissState.targetValue) {
                            DismissValue.Default -> Color.LightGray
                            DismissValue.DismissedToEnd -> Color.Green
                            DismissValue.DismissedToStart -> Color.Red
                        },
                        label = "Color Change"
                    )
                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                    }
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.Default.Done
                        DismissDirection.EndToStart -> Icons.Default.Delete
                    }
                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
                        label = ""
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            icon,
                            contentDescription = "Localized description",
                            modifier = Modifier.scale(scale)
                        )
                    }
                },
                dismissContent = {
                    Card {
                        ListItem(
                            headlineContent = {
                                Text(item.title, fontWeight = if (unread) FontWeight.Bold else null)
                            },
                            supportingContent = { Text("Swipe me left or right!") }
                        )
                    }
                }
            )
        }
    }
}

