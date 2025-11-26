package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import arrow.core.Either
import arrow.core.raise.either
import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.GetUserOutboxCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserOutbox
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.OutboxError
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserOutboxRepository

class GetUserOutboxToPublishUseCase(
    private val repository: UserOutboxRepository,
) : UseCase<GetUserOutboxCommand, OutboxError, List<UserOutbox>>() {
    override suspend fun invoke(input: GetUserOutboxCommand): Either<OutboxError, List<UserOutbox>> =
        either {
            repository.getAllUnpublishedEvents(input.limit).bind()
        }
}
