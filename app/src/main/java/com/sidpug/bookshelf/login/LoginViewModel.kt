package com.sidpug.bookshelf.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sidpug.bookshelf.R
import java.util.UUID

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginWithPasswordFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun onLoginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = FailedLoginFormState(usernameError = R.string.invalid_username)
        } else {
            _loginForm.value = SuccessfulLoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    //  password validation check
    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%&()])(?=\\S+\$).{8,}\$".toRegex()
        return passwordRegex.matches(password)
    }

    fun login(username: String, password: String) {
        if (isUserNameValid(username) && isPasswordValid(password)) {
            // Normally this method would asynchronously send this to your server and your sever
            // would return a token. For high sensitivity apps such as banking, you would keep that
            // token in transient memory. This way the user
            // must login each time they start the app.
            // In this sample, we don't call a server. Instead we use a fake token that we set
            // right here:

            BookShelfAppUser.username = username
            BookShelfAppUser.userId = UUID.randomUUID().toString()
            BookShelfAppUser.fakeToken = UUID.randomUUID().toString()
            _loginResult.value = LoginResult(true)
        } else {
            _loginResult.value = LoginResult(false)
        }
    }
}