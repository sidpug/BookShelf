package com.sidpug.bookshelf.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidpug.bookshelf.R
import com.sidpug.bookshelf.model.CountryResponse
import com.sidpug.bookshelf.network.ApiUtils
import com.sidpug.bookshelf.utility.showLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignUpViewModel : ViewModel() {

    private val _signupForm = MutableLiveData<SignUpState>()
    val signupWithPasswordFormState: LiveData<SignUpState> = _signupForm

    private val _signupResult = MutableLiveData<SignupResult>()
    val signupResult: LiveData<SignupResult> = _signupResult

    private val _countryResult = MutableLiveData<CountryResponse>()
    val countryResult: LiveData<CountryResponse> = _countryResult

    private val _myCountryResult = MutableLiveData<String?>()
    val myCountryResult: LiveData<String?> = _myCountryResult

    fun onSignupDataChanged(username: String, password: String) {
        if (!isEmailValid(username)) {
            _signupForm.value = FailedSignupFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _signupForm.value = FailedSignupFormState(passwordError = R.string.invalid_password)
        } else if (!isNameValid(username)) {
            _signupForm.value = FailedSignupFormState(passwordError = R.string.invalid_password)
        } else {
            _signupForm.value = SuccessfullSignupFormState(isDataValid = true)
        }
    }

    // Username validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    private fun isNameValid(email: String): Boolean {
        return (email.trim().isNotBlank())
    }

    // password validation check
    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%&()])(?=\\S+\$).{8,}\$".toRegex()
        return passwordRegex.matches(password)
    }

    fun signup(email: String, username: String, password: String, country: String?) {
        if (isEmailValid(username) && isPasswordValid(password)) {
            // Normally this method would asynchronously send this to your server and your sever
            // would return a token. For high sensitivity apps such as banking, you would keep that
            // token in transient memory. This way the user
            // must login each time they start the app.
            // In this sample, we don't call a server. Instead we use a fake token that we set
            // right here:

            // add to db

            _signupResult.value = SignupResult(true)
        } else {
            _signupResult.value = SignupResult(false)
        }
    }

    fun getMyCountry() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _myCountryResult.postValue(
                null
            )
            throwable.showLog()
        }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            ApiUtils.ipDetail().getCurrentCountry().apply {
                if (this.status == "success") {
                    _myCountryResult.postValue(this.country)
                    println(this.country)
                } else {
                    _myCountryResult.postValue(null)
                }
            }
        }
    }

    fun getCountryList() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _countryResult.postValue(
                CountryResponse(null, null, null, null, null)
            )
            throwable.showLog()
        }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            ApiUtils.countries().getCountries().apply {
                if (this.statusCode == 200) {
                    _countryResult.postValue(this)
                }
            }
        }
    }
}