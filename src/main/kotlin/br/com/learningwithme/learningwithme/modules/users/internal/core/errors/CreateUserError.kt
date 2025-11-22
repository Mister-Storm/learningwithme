package br.com.learningwithme.learningwithme.modules.users.internal.core.errors

sealed interface CreateUserError : DomainError {
    data object InvalidEmail : CreateUserError

    data object EmailAlreadyExists : CreateUserError

    data object IncorrectDocumentNumber : CreateUserError

    data class PersistenceFailure(
        val reason: String,
    ) : CreateUserError
}
