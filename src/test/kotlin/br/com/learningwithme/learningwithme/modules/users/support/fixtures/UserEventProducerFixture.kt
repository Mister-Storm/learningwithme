package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import arrow.core.Either
import arrow.core.right
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer

object UserEventProducerFixture {
    val SUCCESS_PRODUCER: UserEventProducer = object : DefaultFixture() {}

    private abstract class DefaultFixture : UserEventProducer {
        override fun publishUserCreatedEvent(user: User): Either<CreateUserError, Unit> = Unit.right()

        override fun publishUserUpdatedEvent(user: User): Either<CreateUserError, Unit> = Unit.right()

        override fun publishUserDeletedEvent(user: User): Either<CreateUserError, Unit> = Unit.right()
    }
}
