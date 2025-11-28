package br.com.learningwithme.learningwithme.modules.users.internal.core.errors

sealed interface OutboxError {
    data class PersistenceFailure(
        val reason: String,
    ) : OutboxError
}
