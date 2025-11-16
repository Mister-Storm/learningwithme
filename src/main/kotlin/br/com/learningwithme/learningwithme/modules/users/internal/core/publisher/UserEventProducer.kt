package br.com.learningwithme.learningwithme.modules.users.internal.core.publisher

import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User

interface UserEventProducer {
    fun publishUserCreatedEvent(user: User)

    fun publishUserUpdatedEvent(user: User)

    fun publishUserDeletedEvent(user: User)
}
