package br.com.learningwithme.learningwithme.modules.shared.internal.logging

interface Logger {
    fun debug(
        message: String,
        vararg context: Pair<String, String>,
    )

    fun info(
        message: String,
        vararg context: Pair<String, String>,
    )

    fun warn(
        message: String,
        vararg context: Pair<String, String>,
    )

    fun error(
        message: String,
        vararg context: Pair<String, String>,
    )

    fun withContext(vararg context: Pair<String, String>): Logger
}
