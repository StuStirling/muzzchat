package com.stustirling.muzzchat.data.users

import com.stustirling.muzzchat.core.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUsers(): Flow<List<User>>
}