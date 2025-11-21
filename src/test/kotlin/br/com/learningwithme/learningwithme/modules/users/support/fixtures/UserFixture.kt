package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import br.com.learningwithme.learningwithme.modules.shared.api.Address
import br.com.learningwithme.learningwithme.modules.shared.api.Document
import br.com.learningwithme.learningwithme.modules.shared.api.DocumentType
import br.com.learningwithme.learningwithme.modules.shared.api.Email
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import java.time.Instant
import java.util.UUID

object UserFixture {
    val DEFAULT_USER =
        User(
            id = UUID.randomUUID(),
            email = Email("abc@yahoo.com"),
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
            status = Status.PENDING_CONFIRMATION,
            token = "00000",
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )
    val USER_CONFIRMED =
        DEFAULT_USER.copy(
            status = Status.ENABLED,
        )
}
