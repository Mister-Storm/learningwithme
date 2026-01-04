package br.com.learningwithme.learningwithme.modules.users.internal.async.consumer

import arrow.core.right
import br.com.learningwithme.learningwithme.modules.shared.events.UserEvent
import br.com.learningwithme.learningwithme.modules.users.internal.async.producer.support.extensions.toUserEvent
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.usecase.SendNewUserEmailUseCase
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxFixture.DEFAULT_USER_OUTBOX
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxFixture.USER_OUTBOX_DELETED
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserOutboxFixture.USER_OUTBOX_UPDATED
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class UserEventCreatedConsumerTest {
    @Test
    fun `should call send email use case when event is UserEvent Created`() =
        runTest {
            val event: UserEvent.Created = DEFAULT_USER_OUTBOX.toUserEvent() as UserEvent.Created
            val useCase = mockk<SendNewUserEmailUseCase>()
            val captor = slot<User>()
            coEvery { useCase.invoke(capture(captor)) } returns Unit.right()
            val sut = UserEventCreatedConsumer(useCase)

            sut.onUserEventCreated(event)

            coVerify(exactly = 1) { useCase.invoke(any()) }
            assertEquals(event.id, captor.captured.id)
        }

    @ParameterizedTest
    @MethodSource("eventPublisher")
    fun `should skip event when event is not created`(event: UserEvent) =
        runTest {
            val useCase = mockk<SendNewUserEmailUseCase>()
            coEvery { useCase.invoke(any()) } returns Unit.right()
            val sut = UserEventCreatedConsumer(useCase)

            sut.onUserEventCreated(event)

            coVerify(exactly = 0) { useCase.invoke(any()) }
        }

    companion object {
        @JvmStatic
        fun eventPublisher(): Stream<Arguments> =
            Stream.of(
                Arguments.of(USER_OUTBOX_UPDATED.toUserEvent()),
                Arguments.of(USER_OUTBOX_DELETED.toUserEvent()),
            )
    }
}
