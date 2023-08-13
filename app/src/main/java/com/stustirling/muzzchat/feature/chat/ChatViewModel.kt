package com.stustirling.muzzchat.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stustirling.muzzchat.data.messages.MessagesRepository
import com.stustirling.muzzchat.data.users.UsersRepository
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Content
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Failure
import com.stustirling.muzzchat.feature.chat.messages.MessageItemBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Clock
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val clock: Clock,
    private val usersRepository: UsersRepository,
    private val messagesRepository: MessagesRepository,
    private val messageItemBuilder: MessageItemBuilder
) : ViewModel() {
    private val _uiState = MutableStateFlow<ChatScreenState>(ChatScreenState.Loading)
    internal val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ChatScreenState.Loading
    )

    private val fallbackExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
        _uiState.value = Failure
    }

    // Used to remove the enteredMessage if the sent message
    // is observed as being sent successfully
    private var justSentMessageTimestamp: Long? = null

    init {
        observeRecipient()
    }

    private fun observeRecipient() {
        viewModelScope.launch(fallbackExceptionHandler) {
            usersRepository.getUsers()
                .distinctUntilChanged()
                .collectLatest {
                    val currentUser = it.first { user -> user.isCurrentUser }
                    val recipient = it.first { user -> !user.isCurrentUser }

                    messagesRepository.getMessages(setOf(currentUser.uid, recipient.uid))
                        .map { messages ->
                            messageItemBuilder.buildMessageItems(
                                currentUserId = currentUser.uid,
                                messages = messages
                            )
                        }
                        .collectLatest { messages ->
                            _uiState.value =
                                (uiState.value as? Content)?.let { existingContent ->
                                    existingContent.copy(
                                        messages = messages,
                                        enteredMessage = if (justSentMessageTimestamp == messages.lastOrNull()?.timestamp) {
                                            ""
                                        } else existingContent.enteredMessage
                                    )
                                } ?: Content(
                                    recipient = recipient,
                                    currentAuthor = currentUser,
                                    messages = messages
                                )
                        }
                }
        }
    }
    internal fun onEvent(event: ChatScreenEvent) {
        when (event) {
            is ChatScreenEvent.MessageChanged -> updateEnteredMessage(event.message)
            is ChatScreenEvent.SendMessage -> sendMessage()
            is ChatScreenEvent.SwitchAuthor -> switchAuthor()
        }
    }

    private fun updateEnteredMessage(newMessage: String) {
        val content = (uiState.value as? Content) ?: return
        _uiState.update { content.copy(enteredMessage = newMessage) }
    }

    private fun sendMessage() {
        val content = (uiState.value as? Content) ?: kotlin.run {
            Timber.w("Must have output content before sending a message")
            return
        }

        viewModelScope.launch {
            val timestamp = clock.millis()
            justSentMessageTimestamp = timestamp
            messagesRepository.sendMessage(
                authorId = content.currentAuthor.uid,
                recipientId = content.recipient.uid,
                message = content.enteredMessage,
                timestamp = timestamp
            )
        }
    }

    private fun switchAuthor() {
        val content = (uiState.value as? Content) ?: return
        _uiState.update { content.copyAndSwitchAuthor() }
    }

}