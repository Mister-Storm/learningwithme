package br.com.learningwithme.learningwithme.modules.users.internal.core.adapter

import arrow.core.Either

interface PasswordHashingAdapter {
    suspend fun hashPassword(password: String): Either<PasswordError, String>

    suspend fun verifyPassword(
        plainPassword: String,
        hashedPassword: String,
    ): Either<PasswordError, PasswordCheckResult>
}

sealed interface PasswordError {
    data object HashingError : PasswordError

    data object InvalidPassword : PasswordError
}

sealed interface PasswordCheckResult {
    data object Valid : PasswordCheckResult

    data object ValidNeedsRehash : PasswordCheckResult

    data object Invalid : PasswordCheckResult
}
