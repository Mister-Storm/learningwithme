package br.com.learningwithme.learningwithme.modules.users.internal.async.producer

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Event
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserOutbox
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserOutboxRepository
import org.springframework.stereotype.Component

@Component
class UserEventProducerImpl(
    private val repository: UserOutboxRepository,
) : UserEventProducer {
    override fun publishUserCreatedEvent(user: User): Either<CreateUserError, Unit> =
        either {
            repository
                .save(
                    UserOutbox(
                        user = user,
                        event = Event.CREATED,
                    ),
                ).mapLeft { CreateUserError.PersistenceFailure(it.reason) }
                .bind()
        }

    override fun publishUserUpdatedEvent(user: User): Either<CreateUserError, Unit> = Unit.right()

    override fun publishUserDeletedEvent(user: User): Either<CreateUserError, Unit> = Unit.right()
}
