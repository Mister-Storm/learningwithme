package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.Event
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserOutbox

object UserOutboxFixture {
    val DEFAULT_USER_OUTBOX =
        UserOutbox(
            user = UserFixture.DEFAULT_USER,
            event = Event.CREATED,
        )
    val USER_OUTBOX_UPDATED =
        UserOutbox(
            user = UserFixture.DEFAULT_USER,
            event = Event.UPDATED,
        )
    val USER_OUTBOX_DELETED =
        UserOutbox(
            user = UserFixture.DEFAULT_USER,
            event = Event.DELETED,
        )
}
