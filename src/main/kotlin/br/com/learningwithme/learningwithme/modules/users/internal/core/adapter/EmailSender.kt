package br.com.learningwithme.learningwithme.modules.users.internal.core.adapter

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.SendEmailError
import java.io.File

fun interface EmailSender {
    fun sendEmail(
        to: List<String>,
        subject: String,
        body: String,
        attachment: File?,
    ): Either<SendEmailError, Unit>
}
