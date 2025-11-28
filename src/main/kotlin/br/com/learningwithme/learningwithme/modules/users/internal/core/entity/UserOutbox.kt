package br.com.learningwithme.learningwithme.modules.users.internal.core.entity

import java.util.UUID

data class UserOutbox(
    val id: UUID = UUID.randomUUID(),
    val userEvent: UserEvent,
    val user: User,
    val isPublished: Boolean = false,
)

enum class UserEvent {
    CREATED,
    UPDATED,
    DELETED,
}
