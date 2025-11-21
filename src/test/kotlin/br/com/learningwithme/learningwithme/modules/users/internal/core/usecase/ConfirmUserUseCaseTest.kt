package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserAuth
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.ConfirmUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserResponse
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.ConfirmUserCommandFixture.CONFIRM_COMMAND
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.DbTransactionFixture.DEFAULT_DB_TRANSACTION
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.PasswordHashingAdapterFixture.DEFAULT_PASSWORD_HASH
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.PasswordHashingAdapterFixture.ERROR_PASSWORD_HASH
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.PasswordHashingAdapterFixture.HASHED_PASSWORD
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserEventProducerFixture.SUCCESS_PRODUCER
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserRepositoryFixture.DEFAULT_REPOSITORY
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserRepositoryFixture.WITH_TOKEN_NOT_FOUND
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserRepositoryFixture.WITH_USER_ALREADY_CONFIRMED
import io.mockk.coVerify
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ConfirmUserUseCaseTest {
    @Test
    fun `should return Either left with UserNotFound when token is not valid`() =
        runTest {
            val repository: UserRepository = spyk(WITH_TOKEN_NOT_FOUND)
            val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
            val sut = ConfirmUserUseCase(repository, publisher, DEFAULT_DB_TRANSACTION, DEFAULT_PASSWORD_HASH)
            val result: Either<ConfirmUserError, UserResponse> = sut(CONFIRM_COMMAND)

            assertAll(
                { assertTrue(result.isLeft()) },
                { assertTrue(result.swap().getOrNull() is ConfirmUserError.UserNotFound) },
                { coVerify(exactly = 1) { repository.findByToken(CONFIRM_COMMAND.token) } },
                { coVerify(exactly = 0) { publisher.publishUserUpdatedEvent(any()) } },
                { coVerify(exactly = 0) { repository.update(any()) } },
                { coVerify(exactly = 0) { repository.saveUserAuth(any()) } },
            )
        }

    @Test
    fun `should return Either left with UserAlreadyConfirmed when token is valid but user has already been confirmed`() =
        runTest {
            val repository: UserRepository = spyk(WITH_USER_ALREADY_CONFIRMED)
            val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
            val sut = ConfirmUserUseCase(repository, publisher, DEFAULT_DB_TRANSACTION, DEFAULT_PASSWORD_HASH)
            val result: Either<ConfirmUserError, UserResponse> = sut(CONFIRM_COMMAND)

            assertAll(
                { assertTrue(result.isLeft()) },
                { assertTrue(result.swap().getOrNull() is ConfirmUserError.UserAlreadyConfirmed) },
                { coVerify(exactly = 1) { repository.findByToken(CONFIRM_COMMAND.token) } },
                { coVerify(exactly = 0) { publisher.publishUserUpdatedEvent(any()) } },
                { coVerify(exactly = 0) { repository.update(any()) } },
                { coVerify(exactly = 0) { repository.saveUserAuth(any()) } },
            )
        }

    @Test
    fun `should return Either left with UnexpectedError when token is valid but hash fails`() =
        runTest {
            val repository: UserRepository = spyk(DEFAULT_REPOSITORY)
            val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
            val sut = ConfirmUserUseCase(repository, publisher, DEFAULT_DB_TRANSACTION, ERROR_PASSWORD_HASH)

            val result: Either<ConfirmUserError, UserResponse> = sut(CONFIRM_COMMAND)

            assertAll(
                { assertTrue(result.isLeft()) },
                { assertTrue(result.swap().getOrNull() is ConfirmUserError.UnexpectedError) },
                { coVerify(exactly = 1) { repository.findByToken(CONFIRM_COMMAND.token) } },
                { coVerify(exactly = 0) { publisher.publishUserUpdatedEvent(any()) } },
                { coVerify(exactly = 0) { repository.update(any()) } },
                { coVerify(exactly = 0) { repository.saveUserAuth(any()) } },
            )
        }

    @Test
    fun `should create a new user with status PENDING_CONFIRMATION when email doesnt exists in repository`() =
        runTest {
            val repository: UserRepository = spyk(DEFAULT_REPOSITORY)
            val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
            val userCaptor = slot<User>()
            val userAuthCaptor = slot<UserAuth>()
            val sut = ConfirmUserUseCase(repository, publisher, DEFAULT_DB_TRANSACTION, DEFAULT_PASSWORD_HASH)

            val result: Either<ConfirmUserError, UserResponse> = sut(CONFIRM_COMMAND)

            assertAll(
                { coVerify(exactly = 1) { repository.findByToken(CONFIRM_COMMAND.token) } },
                { coVerify(exactly = 1) { repository.update(capture(userCaptor)) } },
                { coVerify(exactly = 1) { publisher.publishUserUpdatedEvent(userCaptor.captured) } },
                { coVerify(exactly = 1) { repository.saveUserAuth(capture(userAuthCaptor)) } },
                { assertEquals(Status.ENABLED, userCaptor.captured.status) },
                { assertEquals(userCaptor.captured.id, result.getOrNull()?.id) },
                { assertEquals(userCaptor.captured.email, result.getOrNull()?.email) },
                { assertEquals(userCaptor.captured.createdAt, result.getOrNull()?.createdAt) },
                { assertEquals(userCaptor.captured.updatedAt, result.getOrNull()?.updatedAt) },
                { assertEquals(userCaptor.captured.status.name, result.getOrNull()?.status) },
                { assertEquals(userCaptor.captured.id, userAuthCaptor.captured.userId) },
                { assertEquals(userCaptor.captured.email.value, userAuthCaptor.captured.email) },
                { assertEquals(HASHED_PASSWORD, userAuthCaptor.captured.passwordHash) },
            )
        }
}
