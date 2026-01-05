package br.com.learningwithme.learningwithme.modules.users.api.web.controller

import br.com.learningwithme.learningwithme.modules.users.api.web.controller.request.ConfirmUserRequest
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.request.CreateUSerRequest
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {
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

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun createUserSuccess() {
        val request =
            CreateUSerRequest(
                email = "john.doe@example.com",
                firstName = "John",
                documentNumber = "52998224725",
                documentType = "CPF",
                lastName = "Doe",
                street = "Street 1",
                number = "100",
                complement = null,
                district = "District",
                city = "City",
                state = "ST",
                zipCode = "12345000",
            )

        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            ).andExpect(status().isCreated)
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.email").value(request.email))
    }

    @Test
    fun createUserWithExistingEmailReturnsConflict() {
        val request =
            CreateUSerRequest(
                email = "conflict@example.com",
                firstName = "John",
                documentNumber = "52998224725",
                documentType = "CPF",
                lastName = "Doe",
                street = "Street 1",
                number = "100",
                complement = null,
                district = "District",
                city = "City",
                state = "ST",
                zipCode = "12345000",
            )

        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            ).andExpect(status().isCreated)

        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            ).andExpect(status().isConflict)
    }

    @Test
    fun confirmUserSuccess() {
        val createRequest =
            CreateUSerRequest(
                email = "confirm.success@example.com",
                firstName = "Jane",
                documentNumber = "52998224725",
                documentType = "CPF",
                lastName = "Doe",
                street = "Street 1",
                number = "100",
                complement = null,
                district = "District",
                city = "City",
                state = "ST",
                zipCode = "12345000",
            )

        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)),
            ).andExpect(status().isCreated)

        val user =
            kotlinx.coroutines.runBlocking {
                userRepository.findByEmail(createRequest.email).orNull()
            } ?: error("User not found after creation")

        val confirmRequest = ConfirmUserRequest(email = createRequest.email, confirmationCode = user.token, password = "password123")

        mockMvc
            .perform(
                patch("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(confirmRequest)),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("ENABLED"))
    }

    @Test
    fun confirmUserWithInvalidTokenReturnsNotFound() {
        val confirmRequest =
            ConfirmUserRequest(email = "notfound@example.com", confirmationCode = "invalid-token", password = "password123")

        mockMvc
            .perform(
                patch("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(confirmRequest)),
            ).andExpect(status().isNotFound)
    }

    @Test
    fun createUserWithInvalidEmailReturnsBadRequest() {
        val request =
            CreateUSerRequest(
                email = "invalid-email",
                firstName = "John",
                documentNumber = "52998224725",
                documentType = "CPF",
                lastName = "Doe",
                street = "Street 1",
                number = "100",
                complement = null,
                district = "District",
                city = "City",
                state = "ST",
                zipCode = "12345000",
            )

        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
    }

    @Test
    fun createUserWithBlankRequiredFieldsReturnsBadRequest() {
        val request =
            CreateUSerRequest(
                email = " ",
                firstName = "",
                documentNumber = "",
                documentType = "",
                lastName = "",
                street = "",
                number = "",
                complement = null,
                district = "",
                city = "",
                state = "",
                zipCode = "",
            )

        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
    }

    @Test
    fun confirmUserWithInvalidEmailReturnsBadRequest() {
        val request =
            ConfirmUserRequest(
                email = "invalid-email",
                confirmationCode = "some-token",
                password = "password123",
            )

        mockMvc
            .perform(
                patch("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
    }

    @Test
    fun confirmUserWithBlankPasswordReturnsBadRequest() {
        val request =
            ConfirmUserRequest(
                email = "user@example.com",
                confirmationCode = "some-token",
                password = "",
            )

        mockMvc
            .perform(
                patch("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
    }

    @Test
    fun createUserWithInvalidCpfReturnsBadRequestFromDomain() {
        val request =
            CreateUSerRequest(
                email = "domain.invalid.cpf@example.com",
                firstName = "John",
                documentNumber = "12345678900", // invalid per domain validator
                documentType = "CPF",
                lastName = "Doe",
                street = "Street 1",
                number = "100",
                complement = null,
                district = "District",
                city = "City",
                state = "ST",
                zipCode = "12345000",
            )

        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("INVALID_USER_DATA"))
    }

    @Test
    fun confirmUserAlreadyConfirmedReturnsConflict() {
        val createRequest =
            CreateUSerRequest(
                email = "already.confirmed@example.com",
                firstName = "Jane",
                documentNumber = "52998224725",
                documentType = "CPF",
                lastName = "Doe",
                street = "Street 1",
                number = "100",
                complement = null,
                district = "District",
                city = "City",
                state = "ST",
                zipCode = "12345000",
            )

        // create user
        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)),
            ).andExpect(status().isCreated)

        // fetch user and confirm once
        val user =
            kotlinx.coroutines.runBlocking {
                userRepository.findByEmail(createRequest.email).orNull()
            } ?: error("User not found after creation")

        val confirmRequest = ConfirmUserRequest(email = createRequest.email, confirmationCode = user.token, password = "password123")

        mockMvc
            .perform(
                patch("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(confirmRequest)),
            ).andExpect(status().isOk)

        // second confirmation should yield conflict (UserAlreadyConfirmed)
        mockMvc
            .perform(
                patch("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(confirmRequest)),
            ).andExpect(status().isConflict)
            .andExpect(jsonPath("$.code").value("USER_ALREADY_CONFIRMED"))
    }
}
