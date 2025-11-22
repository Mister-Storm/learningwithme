package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.PasswordError
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.PasswordHashingAdapter

object PasswordHashingAdapterFixture {
    const val HASHED_PASSWORD = "hashed_password"
    val DEFAULT_PASSWORD_HASH: PasswordHashingAdapter = object : DefaultPasswordHashingAdapterFixture() {}
    val ERROR_PASSWORD_HASH: PasswordHashingAdapter =
        object : DefaultPasswordHashingAdapterFixture() {
            override suspend fun hashPassword(password: String) = Either.Left(PasswordError.HashingError)
        }

    private abstract class DefaultPasswordHashingAdapterFixture : PasswordHashingAdapter {
        override suspend fun hashPassword(password: String): Either<PasswordError, String> = Either.Right(HASHED_PASSWORD)

        override suspend fun verifyPassword(
            plainPassword: String,
            hashedPassword: String,
        ) = throw NotImplementedError("Not needed for tests")
    }
}
