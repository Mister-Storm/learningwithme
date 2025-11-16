package br.com.learningwithme.learningwithme.modules.users.internal.core

import br.com.learningwithme.learningwithme.modules.shared.api.Email
import java.time.Instant
import java.util.UUID

data class UserCreatedResponse(
    val id: UUID,
    val email: Email,
    val status: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
