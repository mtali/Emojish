package org.mtali.podmoji.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import org.mtali.podmoji.shared.isInputEmoji

@Composable
fun EmojiInputDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onSubmit: (String) -> Unit,
    onInvalidInput: () -> Unit = {}
) {
    if (isVisible) {
        var emojis by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {

            },
            text = {
                OutlinedTextField(
                    label = { Text(text = "Update your emojis") },
                    value = emojis,
                    onValueChange = {
                        if (isInputEmoji(it)) {
                            emojis = it
                        } else {
                            onInvalidInput()
                        }
                    })
            },
            confirmButton = {
                TextButton(onClick = {
                    if (emojis.isNotBlank()) {
                        onSubmit(emojis)
                    }
                    onDismissRequest()
                }) {
                    Text(text = "Submit")
                }
            },
        )
    }
}