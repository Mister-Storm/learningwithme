package br.com.learningwithme.learningwithme.modules.users.support.fixtures

import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.DbTransaction

object DbTransactionFixture {
    val DEFAULT_DB_TRANSACTION: DbTransaction =
        object : DbTransaction {
            override suspend fun <T> runInTransaction(block: suspend () -> T): T = block()
        }
}
