package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer

object UserEventProducerFixture {
    val SUCCESS_PRODUCER: UserEventProducer = object : DefaultFixture() {}

    private abstract class DefaultFixture : UserEventProducer {
        override fun publishUserCreatedEvent(user: User) {}

        override fun publishUserUpdatedEvent(user: User) {}

        override fun publishUserDeletedEvent(user: User) {}
    }
}
