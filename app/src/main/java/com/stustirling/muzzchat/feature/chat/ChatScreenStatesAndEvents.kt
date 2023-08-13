package com.stustirling.muzzchat.feature.chat

import com.stustirling.muzzchat.core.model.User

sealed interface ChatScreenState {
    object Loading : ChatScreenState
    data class Content(
        val messages: List<MessageItem>,
        val currentAuthor: User,
        val recipient: User,
        val enteredMessage: String = ""
    ) : ChatScreenState {
        val currentUser = if (currentAuthor.isCurrentUser) currentAuthor else recipient
        val otherUser = if (!currentAuthor.isCurrentUser) currentAuthor else recipient
        fun copyAndSwitchAuthor() = copy(currentAuthor = recipient, recipient = currentAuthor)
    }

    object Failure : ChatScreenState
}

sealed class ChatScreenEvent {
    data class MessageChanged(val message: String) : ChatScreenEvent()
    object SendMessage : ChatScreenEvent()
    object SwitchAuthor : ChatScreenEvent()
}