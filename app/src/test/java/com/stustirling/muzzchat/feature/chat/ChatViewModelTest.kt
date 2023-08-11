package com.stustirling.muzzchat.feature.chat

import com.stustirling.muzzchat.core.model.Message
import com.stustirling.muzzchat.core.model.User
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Content
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Failure
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Loading
import com.stustirling.muzzchat.testing.TestMessagesRepository
import com.stustirling.muzzchat.testing.TestUserRepository
import io.mockk.coVerify
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

        private val testMessageFromCurrentUser = Message(
            authorId = currentUser.uid,
            recipientId = recipient.uid,
            content = "Test message",
            timestamp = fixedClock.millis(),
            id = 0
        )
    }

    private val userRepository = TestUserRepository()
    private val messagesRepository = TestMessagesRepository()


    private lateinit var viewModel: ChatViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        userRepository.setUsers(listOf(currentUser, recipient))
        viewModel = ChatViewModel(fixedClock, userRepository, messagesRepository)
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
        expected: Message,
        actual: Message
    ) {
        assertEquals(
            expected.authorId,
            actual.authorId
        )
        assertEquals(
            expected.recipientId,
            actual.recipientId
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
            authorId = testMessageFromCurrentUser.authorId,
            recipientId = testMessageFromCurrentUser.recipientId,
            message = testMessageFromCurrentUser.content,
            timestamp = testMessageFromCurrentUser.timestamp
        ); runCurrent()

        assertMessageMatchesIgnoringId(
            testMessageFromCurrentUser,
            (viewModel.uiState.value as? Content)?.messages!!.first()
        )
    }

    @Test
    fun `create message in repo for current user`() = runTest {
        runCurrent()

        viewModel.sendMessage(testMessageFromCurrentUser.content); runCurrent()

        assertMessageMatchesIgnoringId(
            testMessageFromCurrentUser,
            (viewModel.uiState.value as? Content)?.messages!!.first()
        )
    }
}