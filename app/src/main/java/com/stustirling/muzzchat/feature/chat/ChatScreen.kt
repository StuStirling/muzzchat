package com.stustirling.muzzchat.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stustirling.muzzchat.core.model.User
import com.stustirling.muzzchat.ui.theme.MuzzChatTheme
import com.stustirling.muzzchat.ui.theme.MuzzPink

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
        },
        bottomBar = {

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
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentPadding = PaddingValues(12.dp),
                            reverseLayout = true,
                        ) {
                            items(
                                items = chatScreenState.messages.reversed(),
                                key = { message -> message.id }
                            ) { message ->
                                Text(text = message.content)
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, start = 12.dp, end = 12.dp, bottom = 8.dp)
                                    .imePadding(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier.weight(1f),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedIndicatorColor = if (chatScreenState.enteredMessage.isBlank()) Color.LightGray else MuzzPink,
                                        focusedIndicatorColor = MuzzPink,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                                        cursorColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(50),
                                    value = chatScreenState.enteredMessage,
                                    onValueChange = { value ->
                                        onEvent(
                                            ChatScreenEvent.MessageChanged(
                                                value
                                            )
                                        )
                                    })

                                IconButton(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .clip(CircleShape),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MuzzPink,
                                        contentColor = MaterialTheme.colorScheme.onTertiary,
                                        disabledContainerColor = MuzzPink.copy(alpha = .4f)
                                    ),
                                    enabled = chatScreenState.enteredMessage.isNotBlank(),
                                    onClick = { onEvent(ChatScreenEvent.SendMessage) }) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = null
                                    )
                                }
                            }
                        }


                    }
                }
            }
        }
    }
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