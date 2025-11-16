package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import br.com.learningwithme.learningwithme.modules.shared.api.DocumentType
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.CreateUserCommand
import java.util.UUID

object CreateUserCommandFixture {
    val INVALID_CPF_CREATE_COMMAND = createUserCommand()
    val INVALID_EMAIL_CREATE_COMMAND = createUserCommand(documentNumber = "66462778008", email = "invalid-email")
    val VALID_CREATE_COMMAND = createUserCommand(documentNumber = "66462778008")

    fun createUserCommand(
        email: String = "user${UUID.randomUUID().toString().substring(0, 8)}@example.com",
        firstName: String = "John",
        documentNumber: String = "12345678900",
        documentType: DocumentType = DocumentType.CPF,
        lastName: String = "Doe",
        street: String = "Main Street",
        number: String = "123",
        complement: String? = null,
        district: String = "Central District",
        city: String = "São Paulo",
        state: String = "SP",
        zipCode: String = "01234-567",
    ): CreateUserCommand =
        CreateUserCommand(
            email = email,
            firstName = firstName,
            documentNumber = documentNumber,
            documentType = documentType,
            lastName = lastName,
            street = street,
            number = number,
            complement = complement,
            district = district,
            city = city,
            state = state,
            zipCode = zipCode,
        )
}
