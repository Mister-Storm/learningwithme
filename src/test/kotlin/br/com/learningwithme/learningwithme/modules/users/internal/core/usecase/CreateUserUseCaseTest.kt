package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserCreatedResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.exception.EmailAlreadyExistsException
import br.com.learningwithme.learningwithme.modules.users.internal.core.exception.enums.ErrorCode
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.CreateUserCommandFixture.INVALID_CPF_CREATE_COMMAND
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.CreateUserCommandFixture.INVALID_EMAIL_CREATE_COMMAND
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.CreateUserCommandFixture.VALID_CREATE_COMMAND
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserEventProducerFixture.SUCCESS_PRODUCER
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserRepositoryFixture.DEFAULT_REPOSITORY
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserRepositoryFixture.WITH_PREVIOUS_EMAIL
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CreateUserUseCaseTest {
    @Test
    fun `should throw IllegalArgumentException when CPF is invalid`() {
        val repository: UserRepository = spyk(DEFAULT_REPOSITORY)
        val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
        val sut = CreateUserUseCase(repository, publisher)

        val result: Exception = assertThrows<IllegalArgumentException> { sut(INVALID_CPF_CREATE_COMMAND) }
        assertAll(
            { assertEquals("Document number is invalid: ${INVALID_CPF_CREATE_COMMAND.documentNumber}", result.message) },
            { verify(exactly = 1) { repository.findByEmail(INVALID_CPF_CREATE_COMMAND.email) } },
            { verify(exactly = 0) { repository.save(any()) } },
            { verify(exactly = 0) { publisher.publishUserCreatedEvent(any()) } },
        )
    }

    @Test
    fun `should throw IllegalArgumentException when email is invalid`() {
        val repository: UserRepository = spyk(DEFAULT_REPOSITORY)
        val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
        val sut = CreateUserUseCase(repository, publisher)

        val result: Exception = assertThrows<IllegalArgumentException> { sut(INVALID_EMAIL_CREATE_COMMAND) }

        assertAll(
            { assertEquals("Invalid email", result.message) },
            { verify(exactly = 0) { repository.findByEmail(any()) } },
            { verify(exactly = 0) { repository.save(any()) } },
            { verify(exactly = 0) { publisher.publishUserCreatedEvent(any()) } },
        )
    }

    @Test
    fun `should throw EmailAlreadyExistsException when email exists in repository`() {
        val repository: UserRepository = spyk(WITH_PREVIOUS_EMAIL)
        val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
        val sut = CreateUserUseCase(repository, publisher)

        val result: Exception = assertThrows<EmailAlreadyExistsException> { sut(VALID_CREATE_COMMAND) }

        assertAll(
            { assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS.message, result.message) },
            { verify(exactly = 1) { repository.findByEmail(VALID_CREATE_COMMAND.email) } },
            { verify(exactly = 0) { repository.save(any()) } },
            { verify(exactly = 0) { publisher.publishUserCreatedEvent(any()) } },
        )
    }

    @Test
    fun `should create a new user with status PENDING_CONFIRMATION when email doesnt exists in repository`() {
        val repository: UserRepository = spyk(DEFAULT_REPOSITORY)
        val publisher: UserEventProducer = spyk(SUCCESS_PRODUCER)
        val userCaptor = slot<User>()
        val sut = CreateUserUseCase(repository, publisher)

        val result: UserCreatedResponse = sut(VALID_CREATE_COMMAND)

        assertAll(
            { verify(exactly = 1) { repository.findByEmail(VALID_CREATE_COMMAND.email) } },
            { verify(exactly = 1) { repository.save(capture(userCaptor)) } },
            { verify(exactly = 1) { publisher.publishUserCreatedEvent(userCaptor.captured) } },
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
            { assertEquals(userCaptor.captured.id, result.id) },
            { assertEquals(userCaptor.captured.email, result.email) },
            { assertEquals(userCaptor.captured.createdAt, result.createdAt) },
            { assertEquals(userCaptor.captured.updatedAt, result.updatedAt) },
            { assertEquals(userCaptor.captured.status.name, result.status) },
        )
    }
}
