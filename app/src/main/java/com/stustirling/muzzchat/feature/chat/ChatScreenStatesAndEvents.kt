package com.stustirling.muzzchat.feature.chat

import com.stustirling.muzzchat.core.model.Message
import com.stustirling.muzzchat.core.model.User

sealed interface ChatScreenState {
    object Loading: ChatScreenState
    data class Content(
        val currentUser: User,
        val recipient: User,
        val messages: List<MessageItem>,
        val enteredMessage: String = ""
    ) : ChatScreenState

    object Failure: ChatScreenState
}

sealed class ChatScreenEvent {
    data class MessageChanged(val message: String) : ChatScreenEvent()
    object SendMessage : ChatScreenEvent()
}