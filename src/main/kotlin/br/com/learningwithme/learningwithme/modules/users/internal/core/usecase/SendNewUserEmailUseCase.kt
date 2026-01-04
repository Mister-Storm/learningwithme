package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import arrow.core.Either
import arrow.core.raise.either
import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.EmailSender
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.SendEmailError
import br.com.learningwithme.learningwithme.modules.users.internal.core.support.consts.EmailConstants.NEW_USER_BODY
import br.com.learningwithme.learningwithme.modules.users.internal.core.support.consts.EmailConstants.NEW_USER_SUBJECT

class SendNewUserEmailUseCase(
    private val emailSender: EmailSender,
    private val emailLink: String,
) : UseCase<User, SendEmailError, Unit>() {
    override suspend fun invoke(input: User): Either<SendEmailError, Unit> =
        either {
            val log = logger.withContext("user_id" to input.id.toString(), "email" to input.email.value)
            log.info("send-new-user-email invoked")
            emailSender
                .sendEmail(
                    to = listOf(input.email.value),
                    subject = NEW_USER_SUBJECT,
                    body = NEW_USER_BODY.format(emailLink.format(input.token)),
                    attachment = null,
                ).bind()
            log.info("new user email sent")
        }
}
