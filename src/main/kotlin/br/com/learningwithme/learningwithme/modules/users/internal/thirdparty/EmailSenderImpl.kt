package br.com.learningwithme.learningwithme.modules.users.internal.thirdparty

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.EmailSender
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.SendEmailError
import org.springframework.stereotype.Component
import java.io.File

@Component
class EmailSenderImpl : EmailSender {
    override fun sendEmail(
        to: List<String>,
        subject: String,
        body: String,
        attachment: File?,
    ): Either<SendEmailError, Unit> {
        TODO("Not yet implemented")
    }
}
