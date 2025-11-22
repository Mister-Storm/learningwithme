package br.com.learningwithme.learningwithme.modules.users.internal.core.repository

interface DbTransaction {
    suspend fun <R> runInTransaction(block: suspend () -> R): R
}
