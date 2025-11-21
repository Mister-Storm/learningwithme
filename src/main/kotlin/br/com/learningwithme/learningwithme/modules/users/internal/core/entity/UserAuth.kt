package br.com.learningwithme.learningwithme.modules.users.internal.core.entity

import java.time.Instant
import java.util.UUID

data class UserAuth(
    val userId: UUID,
    val email: String,
    val passwordHash: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
)
