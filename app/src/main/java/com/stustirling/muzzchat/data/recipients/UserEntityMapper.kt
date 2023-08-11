package com.stustirling.muzzchat.data.recipients

import com.stustirling.muzzchat.data.database.entities.UserEntity
import com.stustirling.muzzchat.core.model.User
import javax.inject.Inject


class UserEntityMapper @Inject constructor() {
    fun mapEntityToDomain(entity: UserEntity) = User(
        uid = entity.uid,
        isCurrentUser = entity.isCurrentUser,
        name = entity.name,
        imageUrl = entity.imageUrl
    )
}