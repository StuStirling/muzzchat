package com.stustirling.muzzchat.data.recipients

import com.stustirling.muzzchat.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor() : UsersRepository {
    override fun getNonCurrentUser(): Flow<List<User>> {
        TODO("Not yet implemented")
    }
}