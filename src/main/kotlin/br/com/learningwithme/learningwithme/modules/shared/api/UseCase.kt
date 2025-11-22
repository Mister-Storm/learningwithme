package br.com.learningwithme.learningwithme.modules.shared.api

import arrow.core.Either

abstract class UseCase<T, U, V> {
    abstract suspend operator fun invoke(input: T): Either<U, V>
}
