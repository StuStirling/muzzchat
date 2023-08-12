package com.stustirling.muzzchat.feature.chat

import com.stustirling.muzzchat.core.model.Message
import com.stustirling.muzzchat.core.model.User
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Content
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Failure
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Loading
import com.stustirling.muzzchat.testing.TestMessagesRepository
import com.stustirling.muzzchat.testing.TestUserRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.UUID

@ExperimentalCoroutinesApi
class ChatViewModelTest {
    companion object {
        private val recipient = User(
            uid = UUID.randomUUID().toString(),
            isCurrentUser = false,
            name = "Sarah",
            imageUrl = ""
        )
        private val currentUser = User(
            uid = UUID.randomUUID().toString(),
            isCurrentUser = true,
            name = "Stu",
            imageUrl = ""
        )
        private val fixedClock = Clock.fixed(Instant.now(), ZoneOffset.UTC)

        private val testMessageFromCurrentUser = MessageItem(
            id = 0,
            isCurrentUser = true,
            content = "Test message",
            timestamp = fixedClock.millis(),
            showTimeHeading = true,
            showTail = true
        )
    }

    private val userRepository = TestUserRepository()
    private val messagesRepository = TestMessagesRepository()


    private lateinit var viewModel: ChatViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        userRepository.setUsers(listOf(currentUser, recipient))
        viewModel =
            ChatViewModel(fixedClock, userRepository, messagesRepository, MessageItemBuilder())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `output loading as the state upon initial launch`() = runTest {
        assertEquals(Loading, viewModel.uiState.value)
    }

    @Test
    fun `output the chat recipient in the state`() = runTest {
        runCurrent()
        assertEquals(
            recipient,
            (viewModel.uiState.value as? Content)?.recipient
        )
    }

    @Test
    fun `output failure if unable to retrieve any users`() = runTest {
        userRepository.setUsers(emptyList())
        runCurrent()
        assertEquals(Failure, viewModel.uiState.value)
    }

    @Test
    fun `output current user in the state`() = runTest {
        runCurrent()
        assertEquals(
            currentUser,
            (viewModel.uiState.value as? Content)?.currentUser
        )
    }

    private fun assertMessageMatchesIgnoringId(
        expected: MessageItem,
        actual: MessageItem
    ) {
        assertEquals(
            expected.isCurrentUser,
            actual.isCurrentUser
        )
        assertEquals(
            expected.showTail,
            actual.showTail
        )
        assertEquals(
            expected.showTimeHeading,
            actual.showTimeHeading
        )
        assertEquals(
            expected.content,
            actual.content
        )
        assertEquals(
            expected.timestamp,
            actual.timestamp
        )
    }

    @Test
    fun `output messages in state`() = runTest {
        messagesRepository.sendMessage(
            authorId = currentUser.uid,
            recipientId = recipient.uid,
            message = testMessageFromCurrentUser.content,
            timestamp = testMessageFromCurrentUser.timestamp
        ); runCurrent()

        assertMessageMatchesIgnoringId(
            testMessageFromCurrentUser,
            (viewModel.uiState.value as? Content)?.messages!!.first()
        )
    }

    @Test
    fun `update the entered message upon event`() = runTest {
        runCurrent()

        viewModel.onEvent(ChatScreenEvent.MessageChanged("Test message"))
        runCurrent()

        assertEquals(
            "Test message",
            (viewModel.uiState.value as? Content)?.enteredMessage
        )
    }

    @Test
    fun `create message in repo for current user`() = runTest {
        runCurrent()
        viewModel.onEvent(ChatScreenEvent.MessageChanged(testMessageFromCurrentUser.content))
        runCurrent()

        viewModel.onEvent(ChatScreenEvent.SendMessage); runCurrent()

        assertMessageMatchesIgnoringId(
            testMessageFromCurrentUser,
            (viewModel.uiState.value as? Content)?.messages!!.first()
        )
    }

    @Test
    fun `clear the entered message upon send event`() = runTest {
        runCurrent()

        viewModel.onEvent(ChatScreenEvent.MessageChanged("Test message"))
        runCurrent()

        viewModel.onEvent(ChatScreenEvent.SendMessage); runCurrent()

        assertEquals(
            "",
            (viewModel.uiState.value as Content).enteredMessage
        )
    }
}