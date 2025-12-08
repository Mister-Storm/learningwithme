package br.com.learningwithme.learningwithme.modules.shared.internal.transaction

import arrow.core.Either
import arrow.core.raise.either

interface TransactionRunner<E> {
    suspend fun <A> inTransaction(block: suspend () -> A): Either<E, A>
}

suspend fun <E, A> transactional(
    runner: TransactionRunner<E>,
    block: suspend () -> A,
): Either<E, A> = either { runner.inTransaction(block).bind() }
