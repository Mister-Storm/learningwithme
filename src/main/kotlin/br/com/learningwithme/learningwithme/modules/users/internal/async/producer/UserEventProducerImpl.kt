package br.com.learningwithme.learningwithme.modules.users.internal.async.producer

import arrow.core.Either
import arrow.core.right
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer
import org.springframework.stereotype.Component

@Component
class UserEventProducerImpl : UserEventProducer {
    override fun publishUserCreatedEvent(user: User): Either<CreateUserError, Unit> = Unit.right()

    override fun publishUserUpdatedEvent(user: User): Either<CreateUserError, Unit> = Unit.right()

    override fun publishUserDeletedEvent(user: User): Either<CreateUserError, Unit> = Unit.right()
}
