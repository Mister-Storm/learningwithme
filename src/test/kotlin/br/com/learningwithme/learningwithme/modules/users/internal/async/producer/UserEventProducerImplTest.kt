package br.com.learningwithme.learningwithme.modules.users.internal.async.producer

import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserEvent
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserOutbox
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserOutboxRepository
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserFixture.DEFAULT_USER
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxUseCaseFixture.ERROR_OUTBOX_REPOSITORY
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxUseCaseFixture.SUCCESS_OUTBOX_REPOSITORY
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class UserEventProducerImplTest {
    @Test
    fun `should return UserOutbox when entity is saved`() =
        runTest {
            val repository: UserOutboxRepository = spyk(SUCCESS_OUTBOX_REPOSITORY)
            val captor = slot<UserOutbox>()
            val sut = UserEventProducerImpl(repository)
            val user: User = DEFAULT_USER
            val result = sut.publishUserCreatedEvent(user)
            assertAll(
                { assertTrue(result.isRight()) },
                { verify(exactly = 1) { repository.save(capture(captor)) } },
                { assertEquals(UserEvent.CREATED, captor.captured.userEvent) },
                { assertSame(user, captor.captured.user) },
                { assertFalse(captor.captured.isPublished) },
            )
        }

    @Test
    fun `should return OutboxError when repository returns error`() =
        runTest {
            val repository: UserOutboxRepository = spyk(ERROR_OUTBOX_REPOSITORY)
            val sut = UserEventProducerImpl(repository)
            val result = sut.publishUserCreatedEvent(DEFAULT_USER)

            assertAll(
                { assertTrue(result.isLeft()) },
                { assertTrue(result.leftOrNull() is CreateUserError.PersistenceFailure) },
            )
        }
}
