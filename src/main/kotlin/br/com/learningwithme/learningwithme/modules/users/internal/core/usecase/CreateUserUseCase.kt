package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import br.com.learningwithme.learningwithme.modules.shared.api.Email
import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserCreatedResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.CreateUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.exception.EmailAlreadyExistsException
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import br.com.learningwithme.learningwithme.modules.users.internal.core.support.extensions.toUserCreatedResponse

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val publisher: UserEventProducer,
) : UseCase<CreateUserCommand, UserCreatedResponse>() {
    override fun invoke(input: CreateUserCommand): UserCreatedResponse {
        Email.isValid(input.email)
        userRepository.findByEmail(input.email)?.let {
            throw EmailAlreadyExistsException()
        }
        return input
            .toUserEntity()
            .also { user ->
                userRepository.save(user)
                publisher.publishUserCreatedEvent(user)
            }.toUserCreatedResponse()
    }
}
