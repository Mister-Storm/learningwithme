package br.com.learningwithme.learningwithme.modules.users.internal.core.repository

import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User

interface UserRepository {
    fun save(user: User): User

    fun findByEmail(email: String): User?
}
