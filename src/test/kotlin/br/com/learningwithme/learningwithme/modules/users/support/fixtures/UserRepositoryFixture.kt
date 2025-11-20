package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.shared.api.Address
import br.com.learningwithme.learningwithme.modules.shared.api.Document
import br.com.learningwithme.learningwithme.modules.shared.api.DocumentType
import br.com.learningwithme.learningwithme.modules.shared.api.Email
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import java.time.Instant
import java.util.UUID

object UserRepositoryFixture {
    val DEFAULT_REPOSITORY: UserRepository = object : DefaultFixture() {}
    val WITH_PREVIOUS_EMAIL: UserRepository =
        object : DefaultFixture() {
            override suspend fun findByEmail(email: String): Either<CreateUserError, User> =
                Either.Right(
                    User(
                        id = UUID.randomUUID(),
                        email = Email(email),
                        firstName = "Existing",
                        lastName = "User",
                        document =
                            Document(
                                documentType = DocumentType.CPF,
                                value = "66462778008",
                            ),
                        address =
                            Address(
                                street = "X",
                                number = "67",
                                complement = "",
                                district = "",
                                city = "",
                                state = "",
                                zipCode = "88134000",
                            ),
                        status = Status.CREATED,
                        createdAt = Instant.now(),
                        updatedAt = Instant.now(),
                    ),
                )
        }

    private abstract class DefaultFixture : UserRepository {
        override suspend fun save(user: User): Either<CreateUserError, User> = Either.Right(user)

        override suspend fun findByEmail(email: String): Either<CreateUserError, User?> = Either.Right(null)
    }
}
