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
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.support.extensions.toUserResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.support.validators.UserValidator

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val publisher: UserEventProducer,
    private val dbTransaction: DbTransaction,
) : UseCase<CreateUserCommand, CreateUserError, UserResponse>() {
    override suspend fun invoke(input: CreateUserCommand): Either<CreateUserError, UserResponse> =
        either {
            UserValidator.validUser(input).bind()
            userRepository
                .findByEmail(input.email)
                .bind()
                ?.let {
                    raise(CreateUserError.EmailAlreadyExists)
                }
            input
                .toUserEntity()
                .save()
                .bind()
        }

    private suspend fun User.save(): Either<CreateUserError, UserResponse> =
        either {
            dbTransaction.runInTransaction {
                userRepository.save(this@save).bind()
                publisher.publishUserCreatedEvent(this@save).bind()
                this@save.toUserResponse()
            }
        }
}
