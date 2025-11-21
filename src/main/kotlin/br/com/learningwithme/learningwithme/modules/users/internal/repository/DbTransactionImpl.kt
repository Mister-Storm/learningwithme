package br.com.learningwithme.learningwithme.modules.users.internal.repository

import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.DbTransaction
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DbTransactionImpl : DbTransaction {
    @Transactional
    override suspend fun <R> runInTransaction(block: suspend () -> R): R = block()
}