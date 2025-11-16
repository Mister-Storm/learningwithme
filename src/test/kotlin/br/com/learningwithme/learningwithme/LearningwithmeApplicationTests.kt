package br.com.learningwithme.learningwithme

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest
class LearningwithmeApplicationTests {
    companion object {
        @Container
        val postgres: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:15-alpine").apply {
                withDatabaseName("test")
                withUsername("test")
                withPassword("test")
            }

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
        }
    }

    @Test
    fun contextLoads() {
    }
}
