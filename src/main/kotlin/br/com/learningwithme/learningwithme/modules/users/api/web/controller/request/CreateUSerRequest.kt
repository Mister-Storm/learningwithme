package br.com.learningwithme.learningwithme.modules.users.api.web.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CreateUSerRequest(
    @field:Email
    @field:NotBlank
    val email: String,
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val documentNumber: String,
    @field:NotBlank
    val documentType: String,
    @field:NotBlank
    val lastName: String,
    @field:NotBlank
    val street: String,
    @field:NotBlank
    val number: String,
    val complement: String?,
    @field:NotBlank
    val district: String,
    @field:NotBlank
    val city: String,
    @field:NotBlank
    val state: String,
    @field:NotBlank
    val zipCode: String,
)
