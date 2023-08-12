package com.stustirling.muzzchat.feature.chat

import androidx.annotation.VisibleForTesting
import com.stustirling.muzzchat.core.model.Message
import java.time.Duration
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours
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
                    showTimeHeading = index == 0 || previousMessage?.let {
                        Duration.ofMillis(message.timestamp - it.timestamp)
                            .toHours() >= TIME_HEADING_INTERVAL.inWholeHours
                    } == true,
                    showTail = index == sortedMessages.lastIndex || nextMessage?.let {
                        Duration.ofMillis(it.timestamp - message.timestamp)
                            .toSeconds() > TAIL_INTERVAL.inWholeSeconds
                    } == true || nextMessageIsFromOtherUser,
                    isCurrentUser = message.authorId == currentUserId,
                    timestamp = message.timestamp,
                    content = message.content
                )
            }
    }

}