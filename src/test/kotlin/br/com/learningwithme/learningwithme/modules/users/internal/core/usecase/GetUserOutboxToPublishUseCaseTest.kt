package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import br.com.learningwithme.learningwithme.modules.users.internal.core.command.GetUserOutboxCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.OutboxError
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserOutboxRepository
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxUseCaseFixture
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxUseCaseFixture.ERROR_OUTBOX_REPOSITORY
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxUseCaseFixture.SUCCESS_OUTBOX_REPOSITORY
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertSame

class GetUserOutboxToPublishUseCaseTest {
    private val limit = 50

    @Test
    fun `should return UserOutbox list when repository returns success`() =
        runTest {
            val repository: UserOutboxRepository = spyk(SUCCESS_OUTBOX_REPOSITORY)
            val sut = GetUserOutboxToPublishUseCase(repository)

            val result = sut(GetUserOutboxCommand(limit))

            assertAll(
                { assertTrue(result.isRight()) },
                { assertSame(UserOutboxUseCaseFixture.savedOutboxes, result.getOrNull()) },
                { verify(exactly = 1) { repository.getAllUnpublishedEvents(limit) } },
            )
        }

    @Test
    fun `should return OutboxError list when repository returns error`() =
        runTest {
            val repository: UserOutboxRepository = spyk(ERROR_OUTBOX_REPOSITORY)
            val sut = GetUserOutboxToPublishUseCase(repository)

            val result = sut(GetUserOutboxCommand(limit))
            assertAll(
                { assertTrue(result.isLeft()) },
                { assertTrue(result.leftOrNull() is OutboxError.PersistenceFailure) },
                { verify(exactly = 1) { repository.getAllUnpublishedEvents(limit) } },
            )
        }
}
