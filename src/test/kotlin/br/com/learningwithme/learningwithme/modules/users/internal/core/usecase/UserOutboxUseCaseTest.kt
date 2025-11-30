package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserOutbox
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.OutboxError
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserOutboxRepository
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserFixture.DEFAULT_USER
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxCommandFixture.DEFAULT_USER_OUTBOX_COMMAND
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxUseCaseFixture.ERROR_OUTBOX_REPOSITORY
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxUseCaseFixture.SUCCESS_OUTBOX_REPOSITORY
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class UserOutboxUseCaseTest {
    @Test
    fun `should return UserOutbox when entity is saved`() =
        runTest {
            val repository: UserOutboxRepository = spyk(SUCCESS_OUTBOX_REPOSITORY)
            val captor = slot<UserOutbox>()
            val sut = UserOutboxUseCase(repository)
            val result = sut(DEFAULT_USER_OUTBOX_COMMAND)
            assertAll(
                { assertTrue(result.isRight()) },
                { assertEquals(DEFAULT_USER, result.getOrNull()?.user) },
                { assertEquals(DEFAULT_USER_OUTBOX_COMMAND.userEvent, result.getOrNull()?.userEvent) },
                { verify(exactly = 1) { repository.save(capture(captor)) } },
                { assertSame(captor.captured, result.getOrNull()) },
            )
        }

    @Test
    fun `should return OutboxError when repository returns error`() =
        runTest {
            val repository: UserOutboxRepository = spyk(ERROR_OUTBOX_REPOSITORY)
            val sut = UserOutboxUseCase(repository)
            val result = sut(DEFAULT_USER_OUTBOX_COMMAND)

            assertAll(
                { assertTrue(result.isLeft()) },
                { assertTrue(result.leftOrNull() is OutboxError.PersistenceFailure) },
            )
        }
}
