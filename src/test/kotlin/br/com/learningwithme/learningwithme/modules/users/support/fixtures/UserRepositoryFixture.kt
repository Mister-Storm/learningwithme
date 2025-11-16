package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import br.com.learningwithme.learningwithme.modules.shared.api.Address
import br.com.learningwithme.learningwithme.modules.shared.api.Document
import br.com.learningwithme.learningwithme.modules.shared.api.DocumentType
import br.com.learningwithme.learningwithme.modules.shared.api.Email
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import java.time.Instant
import java.util.UUID.randomUUID

object UserRepositoryFixture {
    val DEFAULT_REPOSITORY: UserRepository = object : DefaultFixture() {}
    val WITH_PREVIOUS_EMAIL: UserRepository =
        object : DefaultFixture() {
            override fun findByEmail(email: String): User =
                User(
                    id = randomUUID(),
                    email = Email(email),
                    document =
                        Document(
                            documentType = DocumentType.CPF,
                            value = "66462778008",
                        ),
                    firstName = "Existing",
                    lastName = "User",
                    address =
                        Address(
                            street = "Street",
                            number = "123",
                            complement = "Apt 1",
                            district = "District",
                            city = "City",
                            state = "State",
                            zipCode = "00000-000",
                        ),
                    status = Status.PENDING_CONFIRMATION,
                    createdAt = Instant.now(),
                    updatedAt = Instant.now(),
                )
        }

    private abstract class DefaultFixture : UserRepository {
        override fun save(user: User): User = user

        override fun findByEmail(email: String): User? = null
    }
}
