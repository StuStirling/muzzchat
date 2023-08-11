package com.stustirling.muzzchat.data.recipients

import com.stustirling.muzzchat.data.database.entities.UserEntity
import com.stustirling.muzzchat.model.User
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.UUID

class UserEntityMapperTest {
    private val userEntity = UserEntity(
        uid = UUID.randomUUID().toString(),
        isCurrentUser = true,
        name = "Tester",
        imageUrl = "https://test.jpg"
    )

    private val mapper = UserEntityMapper()

    @Test
    fun `map to user`() {
        assertEquals(
            User(
                uid = userEntity.uid,
                name = userEntity.name,
                imageUrl = userEntity.imageUrl
            ),
            mapper.mapEntityToDomain(userEntity)
        )
    }
}