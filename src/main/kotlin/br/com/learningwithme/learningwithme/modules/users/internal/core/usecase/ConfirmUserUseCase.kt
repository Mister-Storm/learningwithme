package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import arrow.core.Either
import arrow.core.raise.either
import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.PasswordHashingAdapter
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.ConfirmUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserAuth
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.ConfirmUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.DbTransaction
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.support.extensions.toUserResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.support.validators.UserValidator.validStatusToConfirm
import java.time.Instant

class ConfirmUserUseCase(
    private val userRepository: UserRepository,
    private val publisher: UserEventProducer,
    private val dbTransaction: DbTransaction,
    private val passwordHashing: PasswordHashingAdapter,
) : UseCase<ConfirmUserCommand, ConfirmUserError, UserResponse>() {
    override suspend fun invoke(input: ConfirmUserCommand): Either<ConfirmUserError, UserResponse> =
        either {
            val log = logger.withContext("token" to input.token)
            log.info("confirm-user invoked")
            val user = userRepository.findByToken(input.token).bind()
            val userLog = log.withContext("user_id" to user.id.toString(), "email" to user.email.value)
            userLog.debug("user fetched")
            validStatusToConfirm(user).bind()
            passwordHashing
                .hashPassword(input.password)
                .mapLeft {
                    userLog.error("error hashing password")
                    ConfirmUserError.UnexpectedError
                }.bind()
                .let {
                    dbTransaction.runInTransaction {
                        val updatedUser =
                            userRepository
                                .update(
                                    user.copy(
                                        status = Status.ENABLED,
                                        updatedAt = Instant.now(),
                                    ),
                                ).bind()
                        userLog.info("user updated")
                        userRepository
                            .saveUserAuth(
                                UserAuth(
                                    userId = updatedUser.id,
                                    createdAt = Instant.now(),
                                    email = updatedUser.email.value,
                                    passwordHash = it,
                                ),
                            ).bind()
                        userLog.info("user auth created")
                        publisher.publishUserUpdatedEvent(updatedUser)
                        userLog.info("user updated event published")
                        updatedUser.toUserResponse()
                    }
                }
        }
}
