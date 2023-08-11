package com.stustirling.muzzchat.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stustirling.muzzchat.data.recipients.UsersRepository
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Content
import com.stustirling.muzzchat.feature.chat.ChatScreenState.Failure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val usersRepository: UsersRepository
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

    init {
        observeRecipient()
    }

    private fun observeRecipient() {
        viewModelScope.launch(fallbackExceptionHandler) {
            usersRepository.getNonCurrentUsers()
                .map { it.first() }
                .distinctUntilChanged()
                .collectLatest {
                    _uiState.value = Content(recipient = it)
                }
        }
    }


}