package br.com.learningwithme.learningwithme.modules.users.internal.core.command

data class ConfirmUserCommand(
    val token: String,
    val password: String,
)
