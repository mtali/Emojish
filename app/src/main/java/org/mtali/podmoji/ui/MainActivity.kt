package org.mtali.podmoji.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.mtali.podmoji.data.UsersRepo
import org.mtali.podmoji.theme.PodmojiTheme
import org.mtali.podmoji.ui.components.EmojiInputDialog
import org.mtali.podmoji.ui.emoji.EmojiBody
import org.mtali.podmoji.ui.emoji.EmojiViewModel
import org.mtali.podmoji.ui.login.LoginActivity


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        setContent {
            PodmojiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PodmojiApp(onLogout = {
                        auth.signOut()
                        val logoutIntent = Intent(this, LoginActivity::class.java)
                        logoutIntent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(logoutIntent)
                    })
                }
            }
        }
    }
}

@Composable
fun PodmojiApp(
    onLogout: () -> Unit,
    vm: EmojiViewModel = viewModel(factory = EmojiViewModel.Factory(UsersRepo()))
) {
    PodmojiTheme {
        val scaffoldState = rememberScaffoldState()
        val expanded = remember { mutableStateOf(false) }

        var emojiEditDialog by remember { mutableStateOf(false) }

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = { Text(text = "Podmoji") },
                    actions = {

                        IconButton(onClick = { emojiEditDialog = true }) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
                        }

                        Box(Modifier.wrapContentSize(Alignment.TopEnd)) {
                            IconButton(
                                onClick = {
                                    expanded.value = true
                                }
                            ) {
                                Icon(
                                    Icons.Filled.MoreVert,
                                    contentDescription = "Localized description"
                                )
                            }
                            DropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = {
                                    expanded.value = false
                                }) {

                                DropdownMenuItem(onClick = {
                                    expanded.value = false
                                    onLogout()
                                }) {
                                    Text("Logout")
                                }
                            }
                        }
                    }
                )
            },

            ) {
            EmojiBody(vm = vm)

            EmojiInputDialog(
                isVisible = emojiEditDialog,
                onDismissRequest = {
                    emojiEditDialog = false
                },
                onSubmit = { emojis ->
                    vm.updateEmojis(emojis)
                }
            )
        }
    }
}





