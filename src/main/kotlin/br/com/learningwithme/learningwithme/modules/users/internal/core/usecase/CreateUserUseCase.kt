package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import arrow.core.Either
import arrow.core.raise.either
import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.CreateUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.DbTransaction
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserCreatedResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.support.extensions.toUserCreatedResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.support.validators.UserValidator

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val publisher: UserEventProducer,
    private val dbTransaction: DbTransaction,
) : UseCase<CreateUserCommand, Either<CreateUserError, UserCreatedResponse>>() {
    override suspend fun invoke(input: CreateUserCommand): Either<CreateUserError, UserCreatedResponse> =
        either {
            UserValidator.validUser(input).bind()
            val existingEmail: User? = userRepository.findByEmail(input.email).bind()
            if (existingEmail != null) {
                raise(CreateUserError.EmailAlreadyExists)
            }
            input
                .toUserEntity()
                .also { user ->
                    dbTransaction.runInTransaction {
                        userRepository.save(user).bind()
                        publisher.publishUserCreatedEvent(user).bind()
                    }
                }.toUserCreatedResponse()
        }
}
