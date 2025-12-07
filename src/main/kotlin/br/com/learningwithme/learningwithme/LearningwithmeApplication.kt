package br.com.learningwithme.learningwithme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.logging.LogManager

@SpringBootApplication
@EnableAsync
@EnableScheduling
class LearningwithmeApplication

fun main(args: Array<String>) {
    LearningwithmeApplication::class.java.classLoader
        .getResourceAsStream("logging.properties")
        ?.let { LogManager.getLogManager().readConfiguration(it) }
    runApplication<LearningwithmeApplication>(*args)
}
