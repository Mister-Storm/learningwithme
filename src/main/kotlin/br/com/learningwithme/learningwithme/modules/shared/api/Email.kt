package br.com.learningwithme.learningwithme.modules.shared.api

@JvmInline
value class Email(
    val value: String,
) {
    init {
        require(value.isNotEmpty()) { "Email cannot be empty" }
        require(value.contains("@")) { "Invalid email" }
    }
}
