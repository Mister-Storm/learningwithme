package br.com.learningwithme.learningwithme.modules.users.internal.core.errors

sealed interface DomainError {
    data object InvalidEmail : DomainError

    data object EmailAlreadyExists : DomainError

    data class PersistenceFailure(
        val reason: String,
    ) : DomainError
}
