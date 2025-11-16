package br.com.learningwithme.learningwithme.modules.users.internal.core.exception

import br.com.learningwithme.learningwithme.modules.users.internal.core.exception.enums.ErrorCode

class EmailAlreadyExistsException : Exception(ErrorCode.EMAIL_ALREADY_EXISTS.message)
