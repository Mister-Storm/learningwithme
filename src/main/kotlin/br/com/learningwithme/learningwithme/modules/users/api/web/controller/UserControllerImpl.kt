package br.com.learningwithme.learningwithme.modules.users.api.web.controller

import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.mapper.toCommand
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.mapper.toWebResponse
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.request.ConfirmUserRequest
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.request.CreateUSerRequest
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.response.UserResponse
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.spec.UserController
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.ConfirmUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.CreateUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.ConfirmUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError.PersistenceFailure
import jakarta.validation.Valid
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserResponse as DomainUserResponse

@RestController
class UserControllerImpl(
    private val createUserUseCase: UseCase<CreateUserCommand, CreateUserError, DomainUserResponse>,
    private val confirmUserUseCase: UseCase<ConfirmUserCommand, ConfirmUserError, DomainUserResponse>,
) : UserController {
    @PostMapping
    override fun createUser(
        @Valid @RequestBody request: CreateUSerRequest,
    ): ResponseEntity<UserResponse> =
        runBlocking {
            when (val result = createUserUseCase(request.toCommand())) {
                is arrow.core.Either.Right -> {
                    val body = result.value.toWebResponse()
                    val location =
                        ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(body.id)
                            .toUri()
                    ResponseEntity.created(location).body(body)
                }
                is arrow.core.Either.Left ->
                    when (result.value) {
                        CreateUserError.InvalidEmail,
                        CreateUserError.IncorrectDocumentNumber,
                        ->
                            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "INVALID_USER_DATA")
                        CreateUserError.EmailAlreadyExists ->
                            throw ResponseStatusException(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS")
                        is PersistenceFailure ->
                            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "PERSISTENCE_FAILURE")
                    }
            }
        }

    @PatchMapping
    override fun confirmUser(
        @Valid @RequestBody request: ConfirmUserRequest,
    ): ResponseEntity<UserResponse> =
        runBlocking {
            when (val result = confirmUserUseCase(request.toCommand())) {
                is arrow.core.Either.Right -> ResponseEntity.ok(result.value.toWebResponse())
                is arrow.core.Either.Left ->
                    when (result.value) {
                        ConfirmUserError.UserNotFound ->
                            throw ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND")
                        ConfirmUserError.UserAlreadyConfirmed ->
                            throw ResponseStatusException(HttpStatus.CONFLICT, "USER_ALREADY_CONFIRMED")
                        ConfirmUserError.UnexpectedError ->
                            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "UNEXPECTED_ERROR")
                    }
            }
        }
}
