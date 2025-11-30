package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import arrow.core.Either
import arrow.core.raise.either
import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.UserOutboxCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserOutbox
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.OutboxError
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserOutboxRepository

class UserOutboxUseCase(
    private val repository: UserOutboxRepository,
) : UseCase<UserOutboxCommand, OutboxError, UserOutbox>() {
    override suspend fun invoke(input: UserOutboxCommand): Either<OutboxError, UserOutbox> =
        either {
            repository
                .save(
                    UserOutbox(
                        user = input.user,
                        userEvent = input.userEvent,
                    ),
                ).bind()
        }
}
