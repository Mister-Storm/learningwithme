package br.com.learningwithme.learningwithme.modules.users.internal.core.errors

sealed interface SendEmailError {
    data object EmailProviderFailure : SendEmailError
}
