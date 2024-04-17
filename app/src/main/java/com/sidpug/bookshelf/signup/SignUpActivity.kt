package com.sidpug.bookshelf.signup

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import com.sidpug.bookshelf.R
import com.sidpug.bookshelf.booklist.BookListActivity
import com.sidpug.bookshelf.databinding.ActivitySignUpBinding
import com.sidpug.bookshelf.utility.Preferences
import com.sidpug.bookshelf.utility.launchActivity
import com.sidpug.bookshelf.utility.setBoolean


class SignUpActivity : AppCompatActivity() {
    private val TAG = "SignUpActivity"
    private lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel by viewModels<SignUpViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        initObservers()
        initApiCalls()

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupForSignup()
    }

    private fun initObservers() {
        signUpViewModel.countryResult.observe(this) { response ->
            val countries = response.data?.values?.map { it.country }
            val arrayList = countries?.toTypedArray()?.sortedArray()
            arrayList?.let { countryList ->
                setupSpinner(countryList)
            }
        }

        signUpViewModel.myCountryResult.observe(this) { country ->
            signUpViewModel.countryResult.value?.data?.values?.map { it.country }?.toTypedArray()?.sortedArray()?.let { arrayList ->
                val countryAvailable = arrayList.find { it.equals(country, ignoreCase = true) }
                if (countryAvailable != null && !binding.countryDropdown.adapter.isEmpty) {
                    binding.countryDropdown.setSelection(arrayList.indexOf(country))
                }
            }
        }
    }

    private fun initApiCalls() {
        signUpViewModel.getCountryList()
        signUpViewModel.getMyCountry()
    }

    private fun setupSpinner(countryList: Array<String>) {
        val spinner = binding.countryDropdown
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryList)
        spinner.adapter = arrayAdapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }

    private fun setupForSignup() {
        signUpViewModel.signupWithPasswordFormState.observe(this) { formState ->
            val signUpState = formState ?: return@observe
            when (signUpState) {
                is SuccessfullSignupFormState -> binding.signUp.isEnabled = signUpState.isDataValid
                is FailedSignupFormState -> {
                    signUpState.usernameError?.let { binding.username.error = getString(it) }
                    signUpState.passwordError?.let { binding.password.error = getString(it) }
                }
            }
        }
        signUpViewModel.signupResult.observe(this) {
            val signupResult = it ?: return@observe
            if (signupResult.success) {
                Preferences.instance.setBoolean("isLogged", true)
                launchActivity<BookListActivity> {
                    finish()
                }
            }
        }
        binding.email.doAfterTextChanged {
            signUpViewModel.onSignupDataChanged(
                binding.email.text.toString(),
                binding.password.text.toString()
            )
        }
        binding.username.doAfterTextChanged {
            signUpViewModel.onSignupDataChanged(
                binding.username.text.toString(),
                binding.password.text.toString()
            )
        }
        binding.password.doAfterTextChanged {
            signUpViewModel.onSignupDataChanged(
                binding.username.text.toString(),
                binding.password.text.toString()
            )
        }
        binding.password.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    signUpViewModel.signup(
                        binding.email.text.toString(),
                        binding.username.text.toString(),
                        binding.password.text.toString(),
                        binding.countryDropdown[0].toString()
                    )
            }
            false
        }
        binding.signUp.setOnClickListener {
            signUpViewModel.signup(
                binding.email.text.toString(),
                binding.username.text.toString(),
                binding.password.text.toString(),
                binding.countryDropdown[0].toString()
            )
        }
    }
}