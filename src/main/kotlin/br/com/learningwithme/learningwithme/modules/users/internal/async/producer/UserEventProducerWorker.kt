package br.com.learningwithme.learningwithme.modules.users.internal.async.producer

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
@Async
class UserEventProducerWorker
