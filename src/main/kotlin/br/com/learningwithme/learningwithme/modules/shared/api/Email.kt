package br.com.learningwithme.learningwithme.modules.shared.api

@JvmInline
value class Email(
    val value: String,
) {
    companion object {
        fun isValid(email: String): Boolean = email.isNotEmpty() && email.contains("@")
    }
}
