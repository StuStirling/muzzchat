package com.stustirling.muzzchat.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stustirling.muzzchat.core.model.User
import com.stustirling.muzzchat.ui.theme.MuzzChatTheme

@Composable
internal fun ChatRoute(
    onNavigateUp: () -> Unit,
    viewModel: ChatViewModel = viewModel(),
) {
    ChatScreen(
        onNavigateUp = onNavigateUp,
        chatScreenState = viewModel.uiState.collectAsState().value,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ChatScreen(
    onNavigateUp: () -> Unit,
    chatScreenState: ChatScreenState,
    onEvent: (ChatScreenEvent) -> Unit
) {
    Scaffold(
        topBar = {
            ChatTopAppBar(onNavigateUp = onNavigateUp, state = chatScreenState)
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (chatScreenState) {
                ChatScreenState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                ChatScreenState.Failure -> {
                    UnrecoverableErrorDialog(onConfirm = onNavigateUp)
                }

                is ChatScreenState.Content -> {
                    Column(Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            MessageList(
                                modifier = Modifier
                                    .fillMaxSize(),
                                messages = chatScreenState.messages
                            )

                            Shadow(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .rotate(180f)
                            )

                            Shadow(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .fillMaxWidth()
                                    .rotate(0f)
                            )
                        }


                        MessageInputField(
                            modifier = Modifier.fillMaxWidth(),
                            enteredMessage = chatScreenState.enteredMessage,
                            onMessageEntered = { message ->
                                onEvent(
                                    ChatScreenEvent.MessageChanged(
                                        message
                                    )
                                )
                            },
                            onSubmitPressed = { onEvent(ChatScreenEvent.SendMessage) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Shadow(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = .1f),
                        Color.Transparent,
                    )
                )
            )
    )
}

@Composable
private fun UnrecoverableErrorDialog(onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
            ) {
                Text(text = "Ok")
            }
        },
        title = { Text("Error") },
        text = { Text("Something went wrong. Relaunch the app and try again.") }
    )
}

@Composable
@Preview
private fun Preview() {
    MuzzChatTheme {
        ChatScreen(
            onNavigateUp = { /*TODO*/ }, chatScreenState = ChatScreenState.Content(
                currentUser = User("", isCurrentUser = true, name = "Stu", imageUrl = null),
                recipient = User("", isCurrentUser = false, name = "Sarah", imageUrl = null),
                messages = emptyList()
            ),
            onEvent = {}
        )
    }
}

@Composable
@Preview
private fun LoadingPreview() {
    MuzzChatTheme {
        ChatScreen(
            onNavigateUp = { /*TODO*/ }, chatScreenState = ChatScreenState.Content(
                currentUser = User("", isCurrentUser = true, name = "Stu", imageUrl = null),
                recipient = User("", isCurrentUser = false, name = "Sarah", imageUrl = null),
                messages = emptyList()
            ),
            onEvent = {}
        )
    }
}