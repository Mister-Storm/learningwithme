package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserAuth
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.ConfirmUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserFixture.DEFAULT_USER
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserFixture.USER_CONFIRMED

object UserRepositoryFixture {
    val DEFAULT_REPOSITORY: UserRepository = object : DefaultFixture() {}
    val WITH_PREVIOUS_EMAIL: UserRepository =
        object : DefaultFixture() {
            override suspend fun findByEmail(email: String): Either<CreateUserError, User> =
                Either.Right(
                    DEFAULT_USER,
                )
        }
    val WITH_TOKEN_NOT_FOUND: UserRepository =
        object : DefaultFixture() {
            override suspend fun findByToken(token: String): Either<ConfirmUserError, User> = Either.Left(ConfirmUserError.UserNotFound)
        }
    val WITH_USER_ALREADY_CONFIRMED: UserRepository =
        object : DefaultFixture() {
            override suspend fun findByToken(token: String): Either<ConfirmUserError, User> =
                Either.Right(
                    USER_CONFIRMED,
                )
        }

    private abstract class DefaultFixture : UserRepository {
        override suspend fun save(user: User): Either<CreateUserError, User> = Either.Right(user)

        override suspend fun saveUserAuth(userAuth: UserAuth): Either<ConfirmUserError, UserAuth> = Either.Right(userAuth)

        override suspend fun update(user: User): Either<ConfirmUserError, User> = Either.Right(user)

        override suspend fun findByEmail(email: String): Either<CreateUserError, User?> = Either.Right(null)

        override suspend fun findByToken(token: String): Either<ConfirmUserError, User> = Either.Right(DEFAULT_USER)
    }
}
