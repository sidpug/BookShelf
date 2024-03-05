package com.sidpug.bookshelf.signup

/**
 * Data validation state of the login form.
 */
sealed class SignUpState

data class FailedSignupFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null
) : SignUpState()

data class SuccessfullSignupFormState(
    val isDataValid: Boolean = false
) : SignUpState()