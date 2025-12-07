package br.com.learningwithme.learningwithme.modules.shared.internal.logging

import java.util.logging.Logger as JdkLoggerImpl

object LoggerFactory {
    fun getLogger(clazz: Class<*>): Logger {
        val jdk = JdkLoggerImpl.getLogger(clazz.name)
        return JdkLogger(jdk)
    }
}
