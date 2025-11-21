package br.com.learningwithme.learningwithme.modules.users.internal.core.support.extensions

import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserResponse

fun User.toUserResponse() =
    UserResponse(
        id = this.id,
        email = this.email,
        status = this.status.name,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
