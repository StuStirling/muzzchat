package com.stustirling.muzzchat.feature.chat

import com.stustirling.muzzchat.model.User

sealed interface ChatScreenState {
    object Loading: ChatScreenState
    data class Content(
        val recipient: User
    ) : ChatScreenState

    object Failure: ChatScreenState
}