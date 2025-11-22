package br.com.learningwithme.learningwithme.modules.users.api.web.controller.request

data class ConfirmUserRequest(
    val email: String,
    val confirmationCode: String,
)
