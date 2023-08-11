package com.stustirling.muzzchat.testing

import androidx.annotation.VisibleForTesting
import com.stustirling.muzzchat.data.recipients.UsersRepository
import com.stustirling.muzzchat.core.model.User
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@VisibleForTesting
class TestUserRepository : UsersRepository {

    private val usersFlow: MutableSharedFlow<List<User>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getUsers(): Flow<List<User>> = usersFlow

    fun setUsers(users: List<User>) {
        usersFlow.tryEmit(users)
    }
}