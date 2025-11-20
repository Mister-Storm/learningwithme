package br.com.learningwithme.learningwithme.modules.shared.api

abstract class UseCase<T, U> {
    abstract suspend operator fun invoke(input: T): U
}
