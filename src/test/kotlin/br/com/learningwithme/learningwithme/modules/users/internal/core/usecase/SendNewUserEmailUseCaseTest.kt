package br.com.learningwithme.learningwithme.modules.users.internal.core.usecase

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.EmailSender
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.SendEmailError
import br.com.learningwithme.learningwithme.modules.users.internal.core.support.consts.EmailConstants.NEW_USER_BODY
import br.com.learningwithme.learningwithme.modules.users.internal.core.support.consts.EmailConstants.NEW_USER_SUBJECT
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.EmailSenderFixture.FAILURE_SENDER
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.EmailSenderFixture.SUCCESS_SENDER
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserFixture.DEFAULT_USER
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertTrue

private const val EMAIL_LINK = "https://example.com/confirm/%s"

class SendNewUserEmailUseCaseTest {
    @Test
    fun `should return Unit when adapter returns error`() =
        runTest {
            val emailSender: EmailSender = spyk(SUCCESS_SENDER)
            val sut = SendNewUserEmailUseCase(emailSender, emailLink = EMAIL_LINK)
            val result: Either<SendEmailError, Unit> = sut(DEFAULT_USER)
            assertAll(
                { assertTrue(result.isRight()) },
                { assertTrue(result.getOrNull() is Unit) },
                {
                    verify(exactly = 1) {
                        emailSender.sendEmail(
                            to = eq(listOf(DEFAULT_USER.email.value)),
                            subject = NEW_USER_SUBJECT,
                            body = NEW_USER_BODY.format(EMAIL_LINK.format(DEFAULT_USER.token)),
                            attachment = isNull(),
                        )
                    }
                },
            )
        }

    @Test
    fun `should return EmailProviderFailure when adapter returns error`() =
        runTest {
            val emailSender: EmailSender = spyk(FAILURE_SENDER)
            val sut = SendNewUserEmailUseCase(emailSender, EMAIL_LINK)
            val result: Either<SendEmailError, Unit> = sut(DEFAULT_USER)
            assertAll(
                { assertTrue(result.isLeft()) },
                { assertTrue(result.swap().getOrNull() is SendEmailError.EmailProviderFailure) },
            )
        }
}
