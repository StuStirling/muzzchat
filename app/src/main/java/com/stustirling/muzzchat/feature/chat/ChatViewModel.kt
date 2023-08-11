package com.stustirling.muzzchat.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stustirling.muzzchat.data.messages.MessagesRepository
import com.stustirling.muzzchat.data.recipients.UsersRepository
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Content
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Failure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
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
    private val messagesRepository: MessagesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ChatScreenState>(ChatScreenState.Loading)
    internal val uiState = _uiState.onEach { Timber.d("Outputting state: $it ") }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ChatScreenState.Loading
    )

    private val fallbackExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
        _uiState.value = Failure
    }

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
                        .collectLatest { messages ->
                            _uiState.value = Content(
                                recipient = recipient,
                                currentUser = currentUser,
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
            messagesRepository.sendMessage(
                authorId = content.currentUser.uid,
                recipientId = content.recipient.uid,
                message = content.enteredMessage,
                timestamp = clock.millis()
            )

            _uiState.update {
                if (it !is Content) return@update it
                it.copy(enteredMessage = "")
            }
        }
    }


}