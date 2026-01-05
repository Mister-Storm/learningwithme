package br.com.learningwithme.learningwithme.modules.users.api.web.controller.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@RestControllerAdvice(basePackages = ["br.com.learningwithme.learningwithme.modules.users.api.web.controller"])
class UserControllerExceptionHandler {
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<UserErrorResponse> {
        val status = HttpStatus.valueOf(ex.statusCode.value())
        val body =
            UserErrorResponse(
                timestamp = Instant.now(),
                status = status.value(),
                code = ex.reason ?: status.name,
                message = ex.message,
            )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class, BindException::class)
    fun handleValidation(ex: Exception): ResponseEntity<UserErrorResponse> {
        val status = HttpStatus.BAD_REQUEST
        val message =
            when (ex) {
                is MethodArgumentNotValidException ->
                    ex.bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
                is BindException ->
                    ex.bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
                else -> ex.message ?: "Validation error"
            }
        val body =
            UserErrorResponse(
                timestamp = Instant.now(),
                status = status.value(),
                code = "VALIDATION_ERROR",
                message = message,
            )
        return ResponseEntity.status(status).body(body)
    }
}
