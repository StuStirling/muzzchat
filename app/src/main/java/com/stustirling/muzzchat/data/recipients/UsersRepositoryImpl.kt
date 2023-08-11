package com.stustirling.muzzchat.data.recipients

import com.stustirling.muzzchat.data.database.dao.UserDao
import com.stustirling.muzzchat.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userEntityMapper: UserEntityMapper
) : UsersRepository {
    override fun getNonCurrentUsers(): Flow<List<User>> =
        userDao.getNonCurrentUsers()
            .map { it.map { entity -> userEntityMapper.mapEntityToDomain(entity) } }

}