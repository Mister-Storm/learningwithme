package br.com.learningwithme.learningwithme.modules.users.api.config

import br.com.learningwithme.learningwithme.modules.shared.api.UseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.EmailSender
import br.com.learningwithme.learningwithme.modules.users.internal.core.adapter.PasswordHashingAdapter
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.ConfirmUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.command.CreateUserCommand
import br.com.learningwithme.learningwithme.modules.users.internal.core.entity.User
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.ConfirmUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.CreateUserError
import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.SendEmailError
import br.com.learningwithme.learningwithme.modules.users.internal.core.publisher.UserEventProducer
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.DbTransaction
import br.com.learningwithme.learningwithme.modules.users.internal.core.repository.UserRepository
import br.com.learningwithme.learningwithme.modules.users.internal.core.response.UserResponse
import br.com.learningwithme.learningwithme.modules.users.internal.core.usecase.ConfirmUserUseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.usecase.CreateUserUseCase
import br.com.learningwithme.learningwithme.modules.users.internal.core.usecase.SendNewUserEmailUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class UseCaseConfig {
    @Bean
    open fun createUserUseCase(
        userRepository: UserRepository,
        publisher: UserEventProducer,
        dbTransaction: DbTransaction,
    ): UseCase<CreateUserCommand, CreateUserError, UserResponse> =
        CreateUserUseCase(
            userRepository = userRepository,
            publisher = publisher,
            dbTransaction = dbTransaction,
        )

    @Bean
    fun sendNewUserEmailUseCase(
        emailSender: EmailSender,
        @Value("\${app.users.email-link}") emailLink: String = "",
    ): UseCase<User, SendEmailError, Unit> =
        SendNewUserEmailUseCase(
            emailSender = emailSender,
            emailLink = emailLink,
        )

    @Bean
    open fun confirmUserUseCase(
        userRepository: UserRepository,
        publisher: UserEventProducer,
        dbTransaction: DbTransaction,
        passwordHashingAdapter: PasswordHashingAdapter,
    ): UseCase<ConfirmUserCommand, ConfirmUserError, UserResponse> =
        ConfirmUserUseCase(
            userRepository = userRepository,
            publisher = publisher,
            dbTransaction = dbTransaction,
            passwordHashing = passwordHashingAdapter,
        )
}
