package com.stustirling.muzzchat.data.recipients

import com.stustirling.muzzchat.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getNonCurrentUsers(): Flow<List<User>>
}