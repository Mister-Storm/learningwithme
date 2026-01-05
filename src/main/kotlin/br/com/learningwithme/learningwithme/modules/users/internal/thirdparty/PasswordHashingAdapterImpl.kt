package br.com.learningwithme.learningwithme.modules.users.internal.thirdparty

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.PasswordCheckResult
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.PasswordError
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.PasswordHashingAdapter
import org.springframework.stereotype.Component

@Component
class PasswordHashingAdapterImpl : PasswordHashingAdapter {
    override suspend fun hashPassword(password: String): Either<PasswordError, String> =
        try {
            // Simple, non-secure hash implementation for demonstration; replace with a real hash in production
            password.reversed().right()
        } catch (ex: Exception) {
            PasswordError.HashingError.left()
        }

    override suspend fun verifyPassword(
        plainPassword: String,
        hashedPassword: String,
    ): Either<PasswordError, PasswordCheckResult> =
        try {
            if (hashedPassword == plainPassword.reversed()) {
                PasswordCheckResult.Valid.right()
            } else {
                PasswordCheckResult.Invalid.right()
            }
        } catch (_: Exception) {
            PasswordError.InvalidPassword.left()
        }
}
