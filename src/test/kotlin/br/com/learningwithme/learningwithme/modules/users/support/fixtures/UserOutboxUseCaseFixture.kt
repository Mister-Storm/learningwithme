package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserOutbox
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.OutboxError
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserOutboxRepository
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxFixture.DEFAULT_USER_OUTBOX

object UserOutboxUseCaseFixture {
    val savedOutboxes = listOf(DEFAULT_USER_OUTBOX)
    val SUCCESS_OUTBOX_REPOSITORY: UserOutboxRepository =
        object : DefaultUserOutboxUseCase() {
            override fun getAllUnpublishedEvents(limit: Int): Either<OutboxError.PersistenceFailure, List<UserOutbox>> =
                savedOutboxes.right()
        }
    val ERROR_OUTBOX_REPOSITORY: UserOutboxRepository =
        object : DefaultUserOutboxUseCase() {
            override fun save(userOutbox: UserOutbox): Either<OutboxError.PersistenceFailure, UserOutbox> =
                OutboxError.PersistenceFailure("DatabaseError").left()

            override fun delete(userOutbox: UserOutbox): Either<OutboxError.PersistenceFailure, Unit> =
                OutboxError.PersistenceFailure("DatabaseError").left()

            override fun getAllUnpublishedEvents(limit: Int): Either<OutboxError.PersistenceFailure, List<UserOutbox>> =
                OutboxError.PersistenceFailure("DatabaseError").left()
        }

    private abstract class DefaultUserOutboxUseCase : UserOutboxRepository {
        override fun save(userOutbox: UserOutbox): Either<OutboxError.PersistenceFailure, UserOutbox> = userOutbox.right()

        override fun delete(userOutbox: UserOutbox): Either<OutboxError.PersistenceFailure, Unit> = Unit.right()

        override fun getAllUnpublishedEvents(limit: Int): Either<OutboxError.PersistenceFailure, List<UserOutbox>> =
            listOf<UserOutbox>().right()
    }
}
