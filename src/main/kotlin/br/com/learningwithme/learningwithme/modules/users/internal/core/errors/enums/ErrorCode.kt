package br.com.learningwithme.learningwithme.modules.users.internal.core.errors.enums

enum class ErrorCode(
    val code: String,
    val message: String,
) {
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Already exists an user with this email"),
}
