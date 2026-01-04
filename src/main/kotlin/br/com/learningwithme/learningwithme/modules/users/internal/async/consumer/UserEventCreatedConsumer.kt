package br.com.learningwithme.learningwithme.modules.users.internal.async.consumer

import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.shared.events.UserEvent
import br.com.learningwithme.learningwithme.modules.shared.events.support.extensions.toDomainUser
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.SendEmailError
import kotlinx.coroutines.runBlocking
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class UserEventCreatedConsumer(
    private val sendNewUserEmailUseCase: UseCase<User, SendEmailError, Unit>,
) {
    @ApplicationModuleListener
    fun onUserEventCreated(event: UserEvent) {
        if (event is UserEvent.Created) {
            runBlocking { sendNewUserEmailUseCase(event.toDomainUser()) }
        }
    }
}
