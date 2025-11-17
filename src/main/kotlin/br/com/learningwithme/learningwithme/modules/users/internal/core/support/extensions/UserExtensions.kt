package br.com.learningwithme.learningwithme.modules.users.internal.core.support.extensions

import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserCreatedResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User

fun User.toUserCreatedResponse() =
    UserCreatedResponse(
        id = this.id,
        email = this.email,
        status = this.status.name,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
