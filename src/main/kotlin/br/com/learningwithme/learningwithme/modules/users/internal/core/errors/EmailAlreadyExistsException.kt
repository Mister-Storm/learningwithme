package br.com.learningwithme.learningwithme.modules.users.internal.core.errors

import br.com.learningwithme.learningwithme.modules.users.internal.core.errors.enums.ErrorCode

class EmailAlreadyExistsException : Exception(ErrorCode.EMAIL_ALREADY_EXISTS.message)
