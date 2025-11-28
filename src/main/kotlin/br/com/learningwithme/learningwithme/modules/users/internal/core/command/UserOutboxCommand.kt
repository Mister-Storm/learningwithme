package br.com.learningwithme.learningwithme.modules.users.internal.core.command

import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserEvent

data class UserOutboxCommand(
    val userEvent: UserEvent,
    val user: User,
)
