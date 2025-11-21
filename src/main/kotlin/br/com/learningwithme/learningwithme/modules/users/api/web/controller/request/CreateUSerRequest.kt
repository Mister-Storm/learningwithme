package br.com.learningwithme.learningwithme.modules.users.api.web.controller.request

data class CreateUSerRequest(
    val email: String,
    val firstName: String,
    val documentNumber: String,
    val documentType: String,
    val lastName: String,
    val street: String,
    val number: String,
    val complement: String?,
    val district: String,
    val city: String,
    val state: String,
    val zipCode: String,
)
