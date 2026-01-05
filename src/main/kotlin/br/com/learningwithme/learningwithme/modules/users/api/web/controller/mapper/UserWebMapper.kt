package br.com.learningwithme.learningwithme.modules.users.api.web.controller.mapper

import br.com.learningwithme.learningwithme.modules.shared.api.DocumentType
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.request.ConfirmUserRequest
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.request.CreateUSerRequest
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.ConfirmUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.CreateUserCommand
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.response.UserResponse as WebUserResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserResponse as DomainUserResponse

fun CreateUSerRequest.toCommand() =
    CreateUserCommand(
        email = email,
        firstName = firstName,
        documentNumber = documentNumber,
        documentType = DocumentType.valueOf(documentType),
        lastName = lastName,
        street = street,
        number = number,
        complement = complement,
        district = district,
        city = city,
        state = state,
        zipCode = zipCode,
    )

fun ConfirmUserRequest.toCommand() =
    ConfirmUserCommand(
        token = confirmationCode,
        password = this.password,
    )

fun DomainUserResponse.toWebResponse() =
    WebUserResponse(
        id = id,
        email = email,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
