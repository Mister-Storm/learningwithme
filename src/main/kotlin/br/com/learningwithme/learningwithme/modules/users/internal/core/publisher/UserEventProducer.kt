package br.com.learningwithme.learningwithme.modules.users.internal.core.publisher

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError

interface UserEventProducer {
    fun publishUserCreatedEvent(user: User): Either<CreateUserError, Unit>

    fun publishUserUpdatedEvent(user: User): Either<CreateUserError, Unit>

    fun publishUserDeletedEvent(user: User): Either<CreateUserError, Unit>
}
