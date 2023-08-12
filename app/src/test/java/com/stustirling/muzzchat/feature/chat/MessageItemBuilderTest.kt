package com.stustirling.muzzchat.feature.chat

import com.stustirling.muzzchat.core.model.Message
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class MessageItemBuilderTest {
    companion object {
        private const val CURRENT_USER_ID = "current_user_id"
        private const val RECIPIENT_USER_ID = "recipient_user_id"
    }

    private val messageItemBuilder = MessageItemBuilder()

    private fun buildMessage(
        message: String = "default",
        authorId: String = CURRENT_USER_ID,
        recipientId: String = RECIPIENT_USER_ID,
        timestamp: Long
    ) = Message(
        id = 0,
        authorId = authorId,
        recipientId = recipientId,
        content = message,
        timestamp = timestamp
    )

    @Test
    fun `order the message items correctly`() {
        val mostRecent = buildMessage(timestamp = 10)
        val middle = buildMessage(timestamp = 7)
        val oldest = buildMessage(timestamp = 1)

        val messageItems =
            messageItemBuilder.buildMessageItems(
                CURRENT_USER_ID,
                listOf(middle, oldest, mostRecent)
            )

        assertEquals(
            mostRecent.timestamp,
            messageItems[2].timestamp
        )

        assertEquals(
            middle.timestamp,
            messageItems[1].timestamp
        )

        assertEquals(
            oldest.timestamp,
            messageItems[0].timestamp
        )
    }

    @Test
    fun `mark the message item based on current user`() {
        val currentUserMessage = buildMessage(timestamp = 0)
        val recipientMessage = buildMessage(
            authorId = RECIPIENT_USER_ID,
            recipientId = CURRENT_USER_ID,
            timestamp = 10
        )

        val messageItems = messageItemBuilder.buildMessageItems(
            CURRENT_USER_ID,
            listOf(currentUserMessage, recipientMessage)
        )

        assertFalse(messageItems[1].isCurrentUser)
        assertTrue(messageItems[0].isCurrentUser)
    }

    @Test
    fun `show time header when first message`() {
        val firstMessage = buildMessage(timestamp = 0)

        val messageItems = messageItemBuilder.buildMessageItems(
            CURRENT_USER_ID,
            listOf(firstMessage)
        )

        assertTrue(messageItems[0].showTimeHeading)
    }

    @Test
    fun `show time header when x time has passed since previous`() {
        val firstMessage = buildMessage(timestamp = 0)
        val secondMessage =
            buildMessage(timestamp = MessageItemBuilder.TIME_HEADING_INTERVAL.plus(10.minutes).inWholeMilliseconds)

        val messageItems = messageItemBuilder.buildMessageItems(
            CURRENT_USER_ID,
            listOf(firstMessage, secondMessage)
        )

        assertTrue(messageItems[1].showTimeHeading)
    }

    @Test
    fun `do not show time header when within x time of previous message`() {
        val firstMessage = buildMessage(timestamp = 0)
        val secondMessage =
            buildMessage(timestamp = MessageItemBuilder.TIME_HEADING_INTERVAL.minus(10.minutes).inWholeMilliseconds)

        val messageItems = messageItemBuilder.buildMessageItems(
            CURRENT_USER_ID,
            listOf(firstMessage, secondMessage)
        )

        assertFalse(messageItems[1].showTimeHeading)
    }

    @Test
    fun `has tail when most recent message`() {
        val firstMessage = buildMessage(timestamp = 0)
        val secondMessage = buildMessage(timestamp = 10)

        var messageItems = messageItemBuilder.buildMessageItems(
            CURRENT_USER_ID,
            listOf(firstMessage)
        )

        assertTrue(messageItems[0].showTail)

        messageItems = messageItemBuilder.buildMessageItems(
            CURRENT_USER_ID,
            listOf(firstMessage, secondMessage)
        )

        assertFalse(messageItems[0].showTail)
        assertTrue(messageItems[1].showTail)
    }

    @Test
    fun `has tail when x time passed before next message`() {
        val firstMessage = buildMessage(timestamp = 0)
        val secondDelayedMessage =
            buildMessage(timestamp = MessageItemBuilder.TAIL_INTERVAL.plus(5.seconds).inWholeMilliseconds)
        val mostRecentMessage =
            buildMessage(
                timestamp = secondDelayedMessage.timestamp + MessageItemBuilder.TAIL_INTERVAL.minus(
                    3.seconds
                ).inWholeMilliseconds
            )

        val messageItems = messageItemBuilder.buildMessageItems(
            CURRENT_USER_ID,
            listOf(firstMessage, secondDelayedMessage, mostRecentMessage)
        )

        assertTrue(messageItems[0].showTail)
        assertFalse(messageItems[1].showTail)
        assertTrue(messageItems[2].showTail)
    }

    @Test
    fun `has tail as next message not sent by the same user`() {
        val firstMessage = buildMessage(timestamp = 0)
        val messageFromOtherUser =
            buildMessage(
                authorId = RECIPIENT_USER_ID,
                recipientId = CURRENT_USER_ID,
                timestamp = MessageItemBuilder.TAIL_INTERVAL.minus(5.seconds).inWholeMilliseconds
            )
        val mostRecentMessage =
            buildMessage(
                authorId = RECIPIENT_USER_ID,
                recipientId = CURRENT_USER_ID,
                timestamp = messageFromOtherUser.timestamp + 10
            )

        val messageItems = messageItemBuilder.buildMessageItems(
            CURRENT_USER_ID,
            listOf(firstMessage, messageFromOtherUser, mostRecentMessage)
        )

        assertTrue(messageItems[0].showTail)
        assertFalse(messageItems[1].showTail)
        assertTrue(messageItems[2].showTail)
    }


}