package br.com.learningwithme.learningwithme.modules.users.internal.core.repository

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserOutbox
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.OutboxError

interface UserOutboxRepository {
    fun save(userOutbox: UserOutbox): Either<OutboxError.PersistenceFailure, UserOutbox>

    fun delete(userOutbox: UserOutbox): Either<OutboxError.PersistenceFailure, Unit>

    fun getAllUnpublishedEvents(limit: Int): Either<OutboxError.PersistenceFailure, List<UserOutbox>>
}
