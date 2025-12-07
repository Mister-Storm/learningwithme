package br.com.learningwithme.learningwithme.modules.shared.internal.logging

import java.util.logging.Level
import java.util.logging.Logger as JdkLoggerImpl

class JdkLogger(
    private val delegate: JdkLoggerImpl,
    private val baseContext: Map<String, String> = emptyMap(),
) : Logger {
    override fun debug(
        message: String,
        vararg context: Pair<String, String>,
    ) {
        log(Level.FINE, message, context)
    }

    override fun info(
        message: String,
        vararg context: Pair<String, String>,
    ) {
        log(Level.INFO, message, context)
    }

    override fun warn(
        message: String,
        vararg context: Pair<String, String>,
    ) {
        log(Level.WARNING, message, context)
    }

    override fun error(
        message: String,
        vararg context: Pair<String, String>,
    ) {
        log(Level.SEVERE, message, context)
    }

    override fun withContext(vararg context: Pair<String, String>): Logger {
        val merged = baseContext + context.toMap()
        return JdkLogger(delegate, merged)
    }

    private fun log(
        level: Level,
        message: String,
        context: Array<out Pair<String, String>>,
    ) {
        if (!delegate.isLoggable(level)) {
            return
        }
        val ctxMap = baseContext + context.toMap()
        val ctx =
            if (ctxMap.isNotEmpty()) {
                ctxMap.entries.joinToString(
                    separator = ", ",
                ) { "${it.key}=${it.value}" }
            } else {
                ""
            }
        val final = if (ctx.isEmpty()) message else "$message | $ctx"
        delegate.log(level, final)
    }
}
