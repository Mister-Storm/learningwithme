package br.com.learningwithme.learningwithme.modules.users.internal.core.entity

import br.com.learningwithme.learningwithme.modules.shared.api.Address
import br.com.learningwithme.learningwithme.modules.shared.api.Document
import br.com.learningwithme.learningwithme.modules.shared.api.Email
import java.time.Instant
import java.util.UUID

data class User(
    val id: UUID,
    val email: Email,
    val document: Document,
    val firstName: String,
    val lastName: String,
    val address: Address,
    val status: Status,
    val createdAt: Instant,
    val updatedAt: Instant,
)
