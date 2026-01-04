package br.com.learningwithme.learningwithme.modules.shared.events.support.extensions

import br.com.learningwithme.learningwithme.modules.shared.events.UserEvent
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserFixture.DEFAULT_USER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserEventExtensionsKtTest {
    @Test
    fun `should map Created event to domain User with same fields`() {
        val event =
            UserEvent.Created(
                id = DEFAULT_USER.id,
                email = DEFAULT_USER.email,
                document = DEFAULT_USER.document,
                firstName = DEFAULT_USER.firstName,
                lastName = DEFAULT_USER.lastName,
                address = DEFAULT_USER.address,
                status = DEFAULT_USER.status.name,
                token = DEFAULT_USER.token,
                createdAt = DEFAULT_USER.createdAt,
                updatedAt = DEFAULT_USER.updatedAt,
            )

        val user = event.toDomainUser()

        assertThat(user).isEqualTo(DEFAULT_USER)
    }

    @Test
    fun `should map Updated event to domain User with same fields`() {
        val event =
            UserEvent.Updated(
                id = DEFAULT_USER.id,
                email = DEFAULT_USER.email,
                document = DEFAULT_USER.document,
                firstName = DEFAULT_USER.firstName,
                lastName = DEFAULT_USER.lastName,
                address = DEFAULT_USER.address,
                status = DEFAULT_USER.status.name,
                token = DEFAULT_USER.token,
                createdAt = DEFAULT_USER.createdAt,
                updatedAt = DEFAULT_USER.updatedAt,
            )

        val user = event.toDomainUser()

        assertThat(user).isEqualTo(DEFAULT_USER)
    }
}
