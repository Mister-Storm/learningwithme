package br.com.learningwithme.learningwithme.modules.shared.api

data class DomainEvent(
    val type: String,
    val data: Map<String, Any>,
)
