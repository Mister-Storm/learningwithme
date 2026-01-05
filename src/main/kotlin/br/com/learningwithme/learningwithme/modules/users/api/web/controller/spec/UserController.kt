package br.com.learningwithme.learningwithme.modules.users.api.web.controller.spec

import br.com.learningwithme.learningwithme.modules.users.api.web.controller.request.ConfirmUserRequest
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.request.CreateUSerRequest
import br.com.learningwithme.learningwithme.modules.users.api.web.controller.response.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/users")
interface UserController {
    @PostMapping
    fun createUser(
        @RequestBody request: CreateUSerRequest,
    ): ResponseEntity<UserResponse>

    @PatchMapping
    fun confirmUser(
        @RequestBody request: ConfirmUserRequest,
    ): ResponseEntity<UserResponse>
}
