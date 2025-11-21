package br.com.learningwithme.learningwithme.modules.users.internal.core.errors

sealed interface ConfirmUserError : DomainError {
    data object UserNotFound : ConfirmUserError

    data object UserAlreadyConfirmed : ConfirmUserError
}
