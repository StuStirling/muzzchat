package com.stustirling.muzzchat.feature.chat

import com.stustirling.muzzchat.feature.chat.ChatScreenState.Content
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Failure
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Loading
import com.stustirling.muzzchat.model.User
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
import java.util.UUID

@ExperimentalCoroutinesApi
class ChatViewModelTest {
    companion object {
        private val recipient = User(
            id = UUID.randomUUID().toString(),
            name = "Sarah",
            imageUrl = ""
        )
    }

    private val userRepository = TestUserRepository()

    private lateinit var viewModel : ChatViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        userRepository.addNonCurrentUsers(listOf(recipient))
        viewModel = ChatViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `output loading as the state upon initial launch`() = runTest {
        assertEquals(Loading,viewModel.uiState.value)
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
    fun `output failure if unable to retrieve the recipient`() = runTest {
        userRepository.clearNonCurrentUsers()
        runCurrent()
        assertEquals(Failure, viewModel.uiState.value)
    }

}