package br.com.learningwithme.learningwithme.modules.users.internal.core.support.validators

import arrow.core.Either
import arrow.core.raise.either
import br.com.learningwithme.learningwithme.modules.shared.api.Email
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.CreateUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Status
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.ConfirmUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError

object UserValidator {
    fun validUser(command: CreateUserCommand): Either<CreateUserError, Boolean> =
        either {
            if (!Email.isValid(command.email)) {
                raise(CreateUserError.InvalidEmail)
            }
            if (!command.documentType.validate(command.documentNumber)) {
                raise(CreateUserError.IncorrectDocumentNumber)
            }
            true
        }

    fun validStatusToConfirm(user: User): Either<ConfirmUserError, Boolean> =
        either {
            if (user.status != Status.PENDING_CONFIRMATION) {
                raise(ConfirmUserError.UserAlreadyConfirmed)
            }
            true
        }
}
