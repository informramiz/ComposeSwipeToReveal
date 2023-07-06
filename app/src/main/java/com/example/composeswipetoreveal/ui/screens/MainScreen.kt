@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.example.composeswipetoreveal.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val viewState = viewModel.viewState.collectAsState()
    ItemsList(items = viewState.value.listItems) {
        viewModel.onDismissItem(it)
    }
}

@Composable
private fun ItemsList(items: List<MainItem>, onItemDismiss: (MainItem) -> Unit) {
    LazyColumn {
        items(
            items = items,
            key = { it.id }
        ) {
            val updatedItem by rememberUpdatedState(newValue = it)
            val dismissState = rememberDismissState()

            if (dismissState.isDismissed(DismissDirection.EndToStart)
                || dismissState.isDismissed(DismissDirection.StartToEnd)) {
                SideEffect {
                    onItemDismiss(updatedItem)
                }
            }

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
                background = {
                    BackgroundContent(dismissState.dismissDirection)
                },
                dismissContent = {
                    ForegroundRow(item = it)
                }
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
    }
}

@Composable
private fun ForegroundRow(item: MainItem) {
    ListItem(
        headlineContent = {
            Text(item.title)
        },
        supportingContent = { Text("Swipe me left or right!") }
    )
}

@Composable
private fun BackgroundContent(direction: DismissDirection?) {
    direction ?: return

    val icon = when (direction) {
        DismissDirection.EndToStart -> Icons.Default.Delete
        DismissDirection.StartToEnd -> Icons.Default.Done
    }

    val backgroundColor = when (direction) {
        DismissDirection.StartToEnd -> Color.Green
        DismissDirection.EndToStart -> Color.LightGray
    }

    val alignment = when (direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = alignment
    ) {
        Icon(
            modifier = Modifier.padding(horizontal = 20.dp),
            imageVector = icon,
            contentDescription = "delete"
        )
    }
}