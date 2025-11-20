package br.com.learningwithme.learningwithme.modules.users.internal.core.repository

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError

interface UserRepository {
    suspend fun save(user: User): Either<CreateUserError, User>

    suspend fun findByEmail(email: String): Either<CreateUserError, User?>
}
