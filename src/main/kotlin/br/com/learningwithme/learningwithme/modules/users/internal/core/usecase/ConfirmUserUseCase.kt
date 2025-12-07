package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import arrow.core.Either
import arrow.core.raise.either
import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.PasswordHashingAdapter
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.ConfirmUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
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
            logger.info(
                "confirm-user invoked",
                "token" to input.token,
            )
            val user = userRepository.findByToken(input.token).bind()
            logger.debug(
                "user fetched",
                "user_id" to user.id.toString(),
                "email" to user.email.value,
            )
            validStatusToConfirm(user).bind()
            passwordHashing
                .hashPassword(input.password)
                .mapLeft {
                    logger.error(
                        "error hashing password",
                        "user_id" to user.id.toString(),
                    )
                    ConfirmUserError.UnexpectedError
                }.bind()
                .let {
                    dbTransaction.runInTransaction {
                        userRepository
                            .update(
                                updatedUser(user),
                            ).bind()
                            .let { updated ->
                                logger.info(
                                    "user updated",
                                    "user_id" to updated.id.toString(),
                                    "email" to updated.email.value,
                                )
                                updated
                            }.let { updatedUser ->
                                userRepository
                                    .saveUserAuth(
                                        createUserAuth(updatedUser, it),
                                    ).bind()
                                logger.info(
                                    "user auth created",
                                    "user_id" to updatedUser.id.toString(),
                                )
                                publisher.publishUserUpdatedEvent(updatedUser)
                                logger.info(
                                    "user updated event published",
                                    "user_id" to updatedUser.id.toString(),
                                )
                                updatedUser.toUserResponse()
                            }
                    }
                }
        }

    private fun createUserAuth(
        updatedUser: User,
        passwordHash: String,
    ) = UserAuth(
        userId = updatedUser.id,
        createdAt = Instant.now(),
        email = updatedUser.email.value,
        passwordHash = passwordHash,
    )

    private fun updatedUser(user: User) =
        user.copy(
            status = Status.ENABLED,
            updatedAt = Instant.now(),
        )
}
