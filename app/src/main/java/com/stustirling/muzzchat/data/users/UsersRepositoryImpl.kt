package com.stustirling.muzzchat.data.users

import com.stustirling.muzzchat.data.database.dao.UserDao
import com.stustirling.muzzchat.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userEntityMapper: UserEntityMapper
) : UsersRepository {
    override fun getUsers(): Flow<List<User>> =
        userDao.getUsers()
            .map { it.map { entity -> userEntityMapper.mapEntityToDomain(entity) } }

}