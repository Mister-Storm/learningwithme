package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import br.com.learningwithme.learningwithme.modules.users.internal.core.command.ConfirmUserCommand

object ConfirmUserCommandFixture {
    val CONFIRM_COMMAND =
        ConfirmUserCommand(
            token = "command-token",
            password = "command-password",
        )
}
