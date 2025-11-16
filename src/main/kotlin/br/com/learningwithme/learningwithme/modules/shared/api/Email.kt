package br.com.learningwithme.learningwithme.modules.shared.api

@JvmInline
value class Email(
    val value: String,
) {
    init {
        isValid(value)
    }

    companion object {
        fun isValid(email: String) {
            require(email.isNotEmpty()) { "Email cannot be empty" }
            require(email.contains("@")) { "Invalid email" }
        }
    }
}
