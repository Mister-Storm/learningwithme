package br.com.learningwithme.learningwithme.modules.users.api.web.controller.error

import java.time.Instant

data class UserErrorResponse(
    val timestamp: Instant = Instant.now(),
    val status: Int,
    val code: String,
    val message: String,
)
