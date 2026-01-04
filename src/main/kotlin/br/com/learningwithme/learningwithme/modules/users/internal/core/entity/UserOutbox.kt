package br.com.learningwithme.learningwithme.modules.users.internal.core.entity

import java.util.UUID

data class UserOutbox(
    val id: UUID = UUID.randomUUID(),
    val event: Event,
    val user: User,
    val isPublished: Boolean = false,
)

enum class Event {
    CREATED,
    UPDATED,
    DELETED,
}
