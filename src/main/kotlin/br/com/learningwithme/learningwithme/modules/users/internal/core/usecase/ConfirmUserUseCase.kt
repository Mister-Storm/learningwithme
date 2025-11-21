package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.ConfirmUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.ConfirmUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserResponse

class ConfirmUserUseCase : UseCase<ConfirmUserCommand, ConfirmUserError, UserResponse>() {
    override suspend fun invoke(input: ConfirmUserCommand): Either<ConfirmUserError, UserResponse> {
        TODO("Not yet implemented")
    }
}
