package br.com.learningwithme.learningwithme.modules.management.internal.core.entity

import br.com.learningwithme.learningwithme.modules.shared.api.DocumentType

data class ClientOnboarding(
    val email: String,
    val firstName: String,
    val documentNumber: String,
    val documentType: DocumentType,
    val lastName: String,
    val street: String,
    val number: String,
    val complement: String?,
    val district: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val acceptTerms: Boolean,
    val emailConfirmed: Boolean,
)
