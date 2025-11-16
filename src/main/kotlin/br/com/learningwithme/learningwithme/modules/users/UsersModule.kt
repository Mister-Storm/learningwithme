package br.com.learningwithme.learningwithme.modules.users

import org.springframework.modulith.ApplicationModule

@ApplicationModule(displayName = "users", allowedDependencies = ["shared"])
class UsersModule
