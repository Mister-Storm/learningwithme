package br.com.learningwithme.learningwithme.modules.shared.events

import br.com.learningwithme.learningwithme.modules.shared.api.Address
import br.com.learningwithme.learningwithme.modules.shared.api.Document
import br.com.learningwithme.learningwithme.modules.shared.api.Email
import java.time.Instant
import java.util.UUID

sealed interface UserEvent {
    data class Created(
        val id: UUID,
        val email: Email,
        val document: Document,
        val firstName: String,
        val lastName: String,
        val address: Address,
        val status: String,
        val token: String,
        val createdAt: Instant,
        val updatedAt: Instant,
    ) : UserEvent

    data class Updated(
        val id: UUID,
        val email: Email,
        val document: Document,
        val firstName: String,
        val lastName: String,
        val address: Address,
        val status: String,
        val token: String,
        val createdAt: Instant,
        val updatedAt: Instant,
    ) : UserEvent

    data class Deleted(
        val id: UUID,
    ) : UserEvent
}
