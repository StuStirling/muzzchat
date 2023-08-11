package com.stustirling.muzzchat.testing

import androidx.annotation.VisibleForTesting
import com.stustirling.muzzchat.data.recipients.UsersRepository
import com.stustirling.muzzchat.model.User
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@VisibleForTesting
class TestUserRepository : UsersRepository {

    private val nonCurrentUserFlow: MutableSharedFlow<List<User>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getNonCurrentUsers(): Flow<List<User>> = nonCurrentUserFlow

    fun addNonCurrentUsers(users: List<User>) {
        nonCurrentUserFlow.tryEmit(users)
    }

    fun clearNonCurrentUsers() {
        nonCurrentUserFlow.tryEmit(emptyList())
    }
}