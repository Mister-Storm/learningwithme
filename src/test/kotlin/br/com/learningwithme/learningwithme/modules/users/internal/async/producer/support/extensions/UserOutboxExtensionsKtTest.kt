package br.com.learningwithme.learningwithme.modules.users.internal.async.producer.support.extensions

import br.com.learningwithme.learningwithme.modules.shared.events.UserEvent.Created
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxFixture.DEFAULT_USER_OUTBOX
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserOutboxExtensionsKtTest {
    @Test
    fun `should map UserOutbox CREATED event to UserIntegrationEvent_Created with same user fields`() {
        val sut = DEFAULT_USER_OUTBOX

        val event = sut.toUserEvent()

        assertThat(event).isInstanceOf(Created::class.java)
        val created = event as Created

        assertThat(created.id).isEqualTo(sut.user.id)
        assertThat(created.email).isEqualTo(sut.user.email)
        assertThat(created.document).isEqualTo(sut.user.document)
        assertThat(created.firstName).isEqualTo(sut.user.firstName)
        assertThat(created.lastName).isEqualTo(sut.user.lastName)
        assertThat(created.address).isEqualTo(sut.user.address)
        assertThat(created.status).isEqualTo(sut.user.status.name)
        assertThat(created.token).isEqualTo(sut.user.token)
        assertThat(created.createdAt).isEqualTo(sut.user.createdAt)
        assertThat(created.updatedAt).isEqualTo(sut.user.updatedAt)
    }
}
