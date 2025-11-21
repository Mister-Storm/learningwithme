package br.com.learningwithme.learningwithme.modules.users.api.config

import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.CreateUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.DbTransaction
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.usecase.CreateUserUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCaseConfig {
    @Bean
    fun createUserUseCase(
        userRepository: UserRepository,
        publisher: UserEventProducer,
        dbTransaction: DbTransaction,
    ): UseCase<CreateUserCommand, CreateUserError, UserResponse> =
        CreateUserUseCase(
            userRepository = userRepository,
            publisher = publisher,
            dbTransaction = dbTransaction,
        )
}
