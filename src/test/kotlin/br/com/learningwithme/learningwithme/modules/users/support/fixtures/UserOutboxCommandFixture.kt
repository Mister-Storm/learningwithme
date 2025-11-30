package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import br.com.learningwithme.learningwithme.modules.users.internal.core.command.UserOutboxCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserEvent
import br.com.learningwithme.learningwithme.modules.users.support.fixtures.UserFixture.DEFAULT_USER

object UserOutboxCommandFixture {
    val DEFAULT_USER_OUTBOX_COMMAND =
        UserOutboxCommand(
            userEvent = UserEvent.CREATED,
            user = DEFAULT_USER,
        )
}
