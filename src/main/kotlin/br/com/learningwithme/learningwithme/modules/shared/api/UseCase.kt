package br.com.learningwithme.learningwithme.modules.shared.api

import arrow.core.Either
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class UseCase<T, U, V> {
    protected val logger: Logger = LoggerFactory.getLogger(UseCase::class.java)

    abstract suspend operator fun invoke(input: T): Either<U, V>
}
