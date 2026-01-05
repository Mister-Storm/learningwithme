package br.com.learningwithme.learningwithme.modules.users.internal.core.repository

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserAuth
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.ConfirmUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError

interface UserRepository {
    suspend fun save(user: User): Either<CreateUserError, User>

    suspend fun saveUserAuth(userAuth: UserAuth): Either<ConfirmUserError, UserAuth>

    suspend fun update(user: User): Either<ConfirmUserError, User>

    suspend fun findByEmail(email: String): Either<CreateUserError, User?>

    suspend fun findByToken(token: String): Either<ConfirmUserError, User>
}
