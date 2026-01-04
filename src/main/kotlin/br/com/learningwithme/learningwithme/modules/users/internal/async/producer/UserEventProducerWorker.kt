package br.com.learningwithme.learningwithme.modules.users.internal.async.producer

import arrow.core.Either.Left
import arrow.core.Either.Right
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.UserOutbox
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.OutboxError
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserOutboxRepository
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
open class UserEventProducerWorker(
    private val userOutboxRepository: UserOutboxRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {
    private val logger = LoggerFactory.getLogger(UserEventProducerWorker::class.java)

    @Async
    open fun publishPendingUsers() {
        val pendingResult = userOutboxRepository.getAllUnpublishedEvents(50)

        val pending: List<UserOutbox> =
            when (pendingResult) {
                is Left -> {
                    val error: OutboxError.PersistenceFailure = pendingResult.value
                    logger.error("Failed to load pending user outbox entries: {}", error.reason)
                    return
                }
                is Right -> pendingResult.value
            }

        pending.forEach { outbox: UserOutbox ->
            runCatching {
                eventPublisher.publishEvent(outbox.toPublished())
                userOutboxRepository.save(outbox.toPublished())
            }.onFailure { throwable ->
                logger.error("Failed to publish user event {}", outbox.id, throwable)
            }
        }
    }

    private fun UserOutbox.toPublished(): UserOutbox = copy(isPublished = true)
}
