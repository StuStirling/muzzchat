package com.stustirling.muzzchat.feature.chat

import com.stustirling.muzzchat.core.model.Message
import com.stustirling.muzzchat.core.model.User

sealed interface ChatScreenState {
    object Loading: ChatScreenState
    data class Content(
        val currentUser: User,
        val recipient: User,
        val messages: List<Message>
    ) : ChatScreenState

    object Failure: ChatScreenState
}