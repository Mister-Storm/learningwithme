package br.com.learningwithme.learningwithme.modules.users.internal.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserAuth
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserOutbox
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.ConfirmUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.OutboxError
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserOutboxRepository
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryUserRepository :
    UserRepository,
    UserOutboxRepository {
    private val users = ConcurrentHashMap<String, User>()
    private val usersByToken = ConcurrentHashMap<String, User>()
    private val authTable = ConcurrentHashMap<UUID, UserAuth>()

    override suspend fun save(user: User): Either<CreateUserError, User> =
        either {
            if (users.containsKey(user.email.value)) {
                CreateUserError.EmailAlreadyExists.left().bind<User>()
            }

            users[user.email.value] = user

            user.token.let { usersByToken[it] = user }

            user
        }

    override suspend fun saveUserAuth(userAuth: UserAuth): Either<ConfirmUserError, UserAuth> =
        either {
            authTable[userAuth.userId] = userAuth
            userAuth
        }

    override suspend fun update(user: User): Either<ConfirmUserError, User> =
        either {
            val existing =
                users[user.email.value]
                    ?: ConfirmUserError.UserNotFound.left().bind<User>()

            users[user.email.value] = user

            existing.token.let { usersByToken.remove(it) }
            user.token.let { usersByToken[it] = user }

            user
        }

    override suspend fun findByEmail(email: String): Either<CreateUserError, User?> =
        either {
            users[email]
        }

    override suspend fun findByToken(token: String): Either<ConfirmUserError, User> =
        either {
            usersByToken[token]
                ?: ConfirmUserError.UserNotFound.left().bind<User>()
        }

    override fun save(userOutbox: UserOutbox): Either<OutboxError.PersistenceFailure, UserOutbox> = userOutbox.right()

    override fun delete(userOutbox: UserOutbox): Either<OutboxError.PersistenceFailure, Unit> = Unit.right()

    override fun getAllUnpublishedEvents(limit: Int): Either<OutboxError.PersistenceFailure, List<UserOutbox>> =
        listOf<UserOutbox>().right()
}
