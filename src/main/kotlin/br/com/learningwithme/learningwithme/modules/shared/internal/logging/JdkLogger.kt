package br.com.learningwithme.learningwithme.modules.shared.internal.logging

import java.util.logging.Level
import java.util.logging.Logger as JdkLoggerImpl

class JdkLogger(
    private val delegate: JdkLoggerImpl,
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

    private fun log(
        level: Level,
        message: String,
        context: Array<out Pair<String, String>>,
    ) {
        if (!delegate.isLoggable(level)) {
            return
        }
        val ctx =
            if (context.isNotEmpty()) {
                context.joinToString(
                    separator = ", ",
                ) { "${it.first}=${it.second}" }
            } else {
                ""
            }
        val final = if (ctx.isEmpty()) message else "$message | $ctx"
        delegate.log(level, final)
    }
}
