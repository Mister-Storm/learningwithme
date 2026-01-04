package br.com.learningwithme.learningwithme.modules.shared.events.support.extensions

import br.com.learningwithme.learningwithme.modules.shared.events.UserEvent
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User

fun UserEvent.toDomainUser(): User =
    when (this) {
        is UserEvent.Created ->
            User(
                id = id,
                email = email,
                document = document,
                firstName = firstName,
                lastName = lastName,
                address = address,
                status = Status.valueOf(status),
                token = token,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )

        is UserEvent.Updated ->
            User(
                id = id,
                email = email,
                document = document,
                firstName = firstName,
                lastName = lastName,
                address = address,
                status = Status.valueOf(status),
                token = token,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )

        is UserEvent.Deleted -> error("Cannot convert UserEvent.Deleted to domain User")
    }
