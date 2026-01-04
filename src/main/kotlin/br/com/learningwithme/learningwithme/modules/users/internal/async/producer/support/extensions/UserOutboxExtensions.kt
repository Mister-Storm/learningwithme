package br.com.learningwithme.learningwithme.modules.users.internal.async.producer.support.extensions

import br.com.learningwithme.learningwithme.modules.shared.events.UserEvent
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Event
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserOutbox

fun UserOutbox.toUserEvent(): UserEvent =
    when (this.event) {
        Event.CREATED ->
            UserEvent.Created(
                id = this.user.id,
                email = this.user.email,
                document = this.user.document,
                firstName = this.user.firstName,
                lastName = this.user.lastName,
                address = this.user.address,
                status = this.user.status.name,
                token = this.user.token,
                createdAt = this.user.createdAt,
                updatedAt = this.user.updatedAt,
            )

        Event.UPDATED ->
            UserEvent.Updated(
                id = this.user.id,
                email = this.user.email,
                document = this.user.document,
                firstName = this.user.firstName,
                lastName = this.user.lastName,
                address = this.user.address,
                status = this.user.status.name,
                token = this.user.token,
                createdAt = this.user.createdAt,
                updatedAt = this.user.updatedAt,
            )

        Event.DELETED ->
            UserEvent.Deleted(
                id = this.user.id,
            )
    }
