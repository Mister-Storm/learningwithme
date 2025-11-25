package br.com.learningwithme.learningwithme.modules.users.internal.core.support.consts

object EmailConstants {
    const val NEW_USER_SUBJECT = "Welcome to LearningWithMe!"
    const val NEW_USER_BODY = """
        Hello,
        Thank you for registering at LearningWithMe. 
        We're excited to have you on board!
        You need to verify your email address to complete your registration.
        Please click the link below to verify your email:
        %s
        Best regards,
        The LearningWithMe Team
    """
}
