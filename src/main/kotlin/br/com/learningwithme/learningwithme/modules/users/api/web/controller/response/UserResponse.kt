package br.com.learningwithme.learningwithme.modules.users.api.web.controller.response

import br.com.learningwithme.learningwithme.modules.shared.api.Email
import java.time.Instant
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val email: Email,
    val status: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
