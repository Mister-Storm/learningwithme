package br.com.learningwithme.learningwithme.modules.users.internal.core.command

import br.com.learningwithme.learningwithme.modules.shared.api.Address
import br.com.learningwithme.learningwithme.modules.shared.api.Document
import br.com.learningwithme.learningwithme.modules.shared.api.DocumentType
import br.com.learningwithme.learningwithme.modules.shared.api.Email
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import java.time.Instant
import java.util.UUID

data class CreateUserCommand(
    val email: String,
    val firstName: String,
    val documentNumber: String,
    val documentType: DocumentType,
    val lastName: String,
    val street: String,
    val number: String,
    val complement: String?,
    val district: String,
    val city: String,
    val state: String,
    val zipCode: String,
) {
    fun toUserEntity() =
        User(
            UUID.randomUUID(),
            email = Email(this.email),
            document =
                Document(
                    this.documentType,
                    this.documentNumber,
                ),
            firstName = this.firstName,
            lastName = this.lastName,
            address =
                Address(
                    street = this.street,
                    number = this.number,
                    complement = this.complement,
                    district = this.district,
                    city = this.city,
                    state = this.state,
                    zipCode = this.zipCode,
                ),
            status = Status.PENDING_CONFIRMATION,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )
}
