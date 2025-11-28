package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import arrow.core.Either
import arrow.core.right
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.EmailSender
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.SendEmailError
import java.io.File

object EmailSenderFixture {
    val SUCCESS_SENDER: EmailSender = object : DefaultEmailSender() {}
    val FAILURE_SENDER: EmailSender =
        object : DefaultEmailSender() {
            override fun sendEmail(
                to: List<String>,
                subject: String,
                body: String,
                attachment: File?,
            ): Either<SendEmailError, Unit> = Either.Left(SendEmailError.EmailProviderFailure)
        }

    private abstract class DefaultEmailSender : EmailSender {
        override fun sendEmail(
            to: List<String>,
            subject: String,
            body: String,
            attachment: File?,
        ): Either<SendEmailError, Unit> = Unit.right()
    }
}
