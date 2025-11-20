package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository

object UserRepositoryFixture {
    val DEFAULT_REPOSITORY: UserRepository = object : DefaultFixture() {}
    val WITH_PREVIOUS_EMAIL: UserRepository =
        object : DefaultFixture() {
            override suspend fun findByEmail(email: String): Either<CreateUserError, User?> =
                Either.Left(CreateUserError.EmailAlreadyExists)
        }

    private abstract class DefaultFixture : UserRepository {
        override suspend fun save(user: User): Either<CreateUserError, User> = Either.Right(user)

        override suspend fun findByEmail(email: String): Either<CreateUserError, User?> = Either.Right(null)
    }
}
