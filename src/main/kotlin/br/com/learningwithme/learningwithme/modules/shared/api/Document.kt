package br.com.learningwithme.learningwithme.modules.shared.api

data class Document(
    val documentType: DocumentType,
    val value: String,
) {
    init {
        require(documentType.validate(value)) { "Document number is invalid: $value" }
    }
}
