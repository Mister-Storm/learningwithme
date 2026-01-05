package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import arrow.core.right
import br.com.learningwithme.learningwithme.modules.shared.api.Address
import br.com.learningwithme.learningwithme.modules.shared.api.Document
import br.com.learningwithme.learningwithme.modules.shared.api.DocumentType
import br.com.learningwithme.learningwithme.modules.shared.api.Email
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.EmailSender
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Instant
import java.util.UUID

@Testcontainers
@SpringBootTest
class SendNewUserEmailUseCaseIT {
    companion object {
        @Container
        val postgres: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:15-alpine").apply {
                withDatabaseName("test")
                withUsername("test")
                withPassword("test")
            }

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
        }
    }

    private class RecordingEmailSender : EmailSender {
        val calls = mutableListOf<Triple<List<String>, String, String>>()

        override fun sendEmail(
            to: List<String>,
            subject: String,
            body: String,
            attachment: java.io.File?,
        ) = kotlin.run {
            calls.add(Triple(to, subject, body))
            Unit.right()
        }
    }

    @Test
    fun sendEmailSuccess() {
        val recordingSender = RecordingEmailSender()
        // temporarily invoke use case logic directly using the recording sender
        val localUseCase = SendNewUserEmailUseCase(recordingSender, "http://test-link")

        val user =
            User(
                id = UUID.randomUUID(),
                email = Email("email.success@example.com"),
                document =
                    Document(
                        documentType = DocumentType.CPF,
                        value = "52998224725",
                    ),
                firstName = "Test",
                lastName = "User",
                address =
                    Address(
                        street = "Street 1",
                        number = "100",
                        complement = null,
                        district = "District",
                        city = "City",
                        state = "ST",
                        zipCode = "12345000",
                    ),
                status = Status.PENDING_CONFIRMATION,
                token = "token",
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            )

        kotlinx.coroutines.runBlocking {
            localUseCase(user)
        }

        assertTrue(recordingSender.calls.isNotEmpty())
        assertTrue(
            recordingSender.calls
                .first()
                .first
                .contains(user.email.value),
        )
    }
}
