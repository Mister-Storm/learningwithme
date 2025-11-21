package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserCreatedResponse
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.CreateUserCommandFixture.INVALID_CPF_CREATE_COMMAND
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.CreateUserCommandFixture.INVALID_EMAIL_CREATE_COMMAND
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.CreateUserCommandFixture.VALID_CREATE_COMMAND
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.DbTransactionFixture.DEFAULT_DB_TRANSACTION
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserEventProducerFixture.SUCCESS_PRODUCER
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserRepositoryFixture.DEFAULT_REPOSITORY
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserRepositoryFixture.WITH_PREVIOUS_EMAIL
import io.mockk.coVerify
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals

class CreateUserUseCaseTest {
    @Test
    fun `should return Either left with IncorrectDocumentNumber when CPF is invalid`() =
        runTest {
            val repository: UserRepository = spyk(DEFAULT_REPOSITORY)
            val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
            val sut = CreateUserUseCase(repository, publisher, DEFAULT_DB_TRANSACTION)

            val result: Either<CreateUserError, UserCreatedResponse> = sut(INVALID_CPF_CREATE_COMMAND)
            assertAll(
                { assertTrue(result.isLeft()) },
                { assertTrue(result.leftOrNull() is CreateUserError.IncorrectDocumentNumber) },
                { coVerify(exactly = 0) { repository.findByEmail(any()) } },
                { coVerify(exactly = 0) { repository.save(any()) } },
                { coVerify(exactly = 0) { publisher.publishUserCreatedEvent(any()) } },
            )
        }

    @Test
    fun `should return Either left with InvalidEmail when email is invalid`() =
        runTest {
            val repository: UserRepository = spyk(DEFAULT_REPOSITORY)
            val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
            val sut = CreateUserUseCase(repository, publisher, DEFAULT_DB_TRANSACTION)

            val result: Either<CreateUserError, UserCreatedResponse> = sut(INVALID_EMAIL_CREATE_COMMAND)

            assertAll(
                { assertTrue(result.isLeft()) },
                { assertTrue(result.leftOrNull() is CreateUserError.InvalidEmail) },
                { coVerify(exactly = 0) { repository.findByEmail(any()) } },
                { coVerify(exactly = 0) { repository.save(any()) } },
                { coVerify(exactly = 0) { publisher.publishUserCreatedEvent(any()) } },
            )
        }

    @Test
    fun `should return Either left with EmailAlreadyExists when email exists in repository`() =
        runTest {
            val repository: UserRepository = spyk(WITH_PREVIOUS_EMAIL)
            val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
            val sut = CreateUserUseCase(repository, publisher, DEFAULT_DB_TRANSACTION)

            val result: Either<CreateUserError, UserCreatedResponse> = sut(VALID_CREATE_COMMAND)

            assertAll(
                { assertTrue(result.isLeft()) },
                { assertTrue(result.leftOrNull() is CreateUserError.EmailAlreadyExists) },
                { coVerify(exactly = 1) { repository.findByEmail(VALID_CREATE_COMMAND.email) } },
                { coVerify(exactly = 0) { repository.save(any()) } },
                { coVerify(exactly = 0) { publisher.publishUserCreatedEvent(any()) } },
            )
        }

    @Test
    fun `should create a new user with status PENDING_CONFIRMATION when email doesnt exists in repository`() =
        runTest {
            val repository: UserRepository = spyk(DEFAULT_REPOSITORY)
            val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
            val userCaptor = slot<User>()
            val sut = CreateUserUseCase(repository, publisher, DEFAULT_DB_TRANSACTION)

            val result: Either<CreateUserError, UserCreatedResponse> = sut(VALID_CREATE_COMMAND)

            assertAll(
                { coVerify(exactly = 1) { repository.findByEmail(VALID_CREATE_COMMAND.email) } },
                { coVerify(exactly = 1) { repository.save(capture(userCaptor)) } },
                { coVerify(exactly = 1) { publisher.publishUserCreatedEvent(userCaptor.captured) } },
                { assertEquals(VALID_CREATE_COMMAND.email, userCaptor.captured.email.value) },
                { assertEquals(VALID_CREATE_COMMAND.documentType, userCaptor.captured.document.documentType) },
                { assertEquals(VALID_CREATE_COMMAND.documentNumber, userCaptor.captured.document.value) },
                { assertEquals(VALID_CREATE_COMMAND.firstName, userCaptor.captured.firstName) },
                { assertEquals(VALID_CREATE_COMMAND.lastName, userCaptor.captured.lastName) },
                { assertEquals(VALID_CREATE_COMMAND.street, userCaptor.captured.address.street) },
                { assertEquals(VALID_CREATE_COMMAND.number, userCaptor.captured.address.number) },
                { assertEquals(VALID_CREATE_COMMAND.complement, userCaptor.captured.address.complement) },
                { assertEquals(VALID_CREATE_COMMAND.district, userCaptor.captured.address.district) },
                { assertEquals(VALID_CREATE_COMMAND.city, userCaptor.captured.address.city) },
                { assertEquals(VALID_CREATE_COMMAND.state, userCaptor.captured.address.state) },
                { assertEquals(VALID_CREATE_COMMAND.zipCode, userCaptor.captured.address.zipCode) },
                { assertEquals(Status.PENDING_CONFIRMATION, userCaptor.captured.status) },
                { assertEquals(userCaptor.captured.id, result.getOrNull()?.id) },
                { assertEquals(userCaptor.captured.email, result.getOrNull()?.email) },
                { assertEquals(userCaptor.captured.createdAt, result.getOrNull()?.createdAt) },
                { assertEquals(userCaptor.captured.updatedAt, result.getOrNull()?.updatedAt) },
                { assertEquals(userCaptor.captured.status.name, result.getOrNull()?.status) },
            )
        }
}
