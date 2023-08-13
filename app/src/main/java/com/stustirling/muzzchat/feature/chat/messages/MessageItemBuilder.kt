package com.stustirling.muzzchat.feature.chat.messages

import androidx.annotation.VisibleForTesting
import com.stustirling.muzzchat.core.model.Message
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class MessageItemBuilder @Inject constructor() {
    companion object {
        @VisibleForTesting
        internal val TIME_HEADING_INTERVAL = 1.hours

        @VisibleForTesting
        internal val TAIL_INTERVAL = 20.seconds
    }

    fun buildMessageItems(
        currentUserId: String,
        messages: List<Message>
    ): List<MessageItem> {
        val sortedMessages = messages.sortedBy(Message::timestamp)
        return sortedMessages
            .mapIndexed { index, message ->
                val previousMessage = if (index > 0) sortedMessages[index - 1] else null
                val nextMessage =
                    if (index < sortedMessages.lastIndex) sortedMessages[index + 1] else null

                val nextMessageIsFromOtherUser =
                    nextMessage?.authorId != null && nextMessage.authorId != message.authorId

                MessageItem(
                    id = message.id,
                    showTimeHeading = index == 0 || durationHasElapsedSincePreviousMessage(
                        previousMessage = previousMessage,
                        message = message,
                        duration = TIME_HEADING_INTERVAL
                    ),
                    showTail = index == sortedMessages.lastIndex || durationWillPassBeforeNextMessage(
                        nextMessage = nextMessage,
                        message = message,
                        duration = TAIL_INTERVAL
                    ) || nextMessageIsFromOtherUser,
                    isCurrentUser = message.authorId == currentUserId,
                    timestamp = message.timestamp,
                    content = message.content
                )
            }
    }

    private fun durationHasElapsedSincePreviousMessage(
        previousMessage: Message?,
        message: Message,
        duration: Duration
    ) =
        previousMessage?.let {
            (message.timestamp - it.timestamp).milliseconds > duration
        } == true

    private fun durationWillPassBeforeNextMessage(
        nextMessage: Message?,
        message: Message,
        duration: Duration
    ) =
        nextMessage?.let {
            (it.timestamp - message.timestamp).milliseconds > duration
        } == true
}