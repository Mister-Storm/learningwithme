package br.com.learningwithme.learningwithme.modules.users.api.web.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class ConfirmUserRequest(
    @field:Email
    @field:NotBlank
    val email: String,
    @field:NotBlank
    val confirmationCode: String,
    @field:NotBlank
    val password: String,
)
