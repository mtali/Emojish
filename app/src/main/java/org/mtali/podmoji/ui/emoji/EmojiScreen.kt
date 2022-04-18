package org.mtali.podmoji.ui.emoji

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mtali.podmoji.api.State
import org.mtali.podmoji.ui.components.UserListItem

@Composable
fun EmojiBody(vm: EmojiViewModel) {
    val state = vm.users.observeAsState()
    when (state.value) {
        is State.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is State.Success -> {
            val users = (state.value!! as State.Success).data

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(users) { user ->
                    UserListItem(user = user)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        else -> {
            if (state.value != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Failed")
                }
            }
        }
    }
}