package br.com.learningwithme.learningwithme.modules.shared.api

import arrow.core.Either
import br.com.learningwithme.learningwithme.modules.shared.internal.logging.Logger
import br.com.learningwithme.learningwithme.modules.shared.internal.logging.LoggerFactory

abstract class UseCase<T, U, V> {
    protected val logger: Logger = LoggerFactory.getLogger(javaClass)

    abstract suspend operator fun invoke(input: T): Either<U, V>
}
