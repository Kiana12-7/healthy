package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        initObservers()
        initInputListeners()
        initClickListeners()
    }

    private fun initObservers() {
        loginViewModel.loginFormState.observe(this) {
            val loginState = it ?: return@observe
            binding.btnLogin.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                binding.etUsername.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                binding.etPassword.error = getString(loginState.passwordError)
            }
        }

        loginViewModel.loginResult.observe(this) {
            val loginResult = it ?: return@observe
            binding.loading.visibility = View.GONE
            if (loginResult.error != null) showLoginFailed(loginResult.error)
            if (loginResult.success != null) updateUiWithUser(loginResult.success)
            setResult(RESULT_OK)
        }
    }

    private fun initInputListeners() {
        binding.etUsername.afterTextChanged { checkForm() }
        binding.etPassword.afterTextChanged { checkForm() }

//        binding.cbAgree.setOnCheckedChangeListener { _, _ -> checkForm() }

        binding.etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.btnLogin.isEnabled) {
                doLogin()
            }
            false
        }
    }

    private fun initClickListeners() {
        binding.btnLogin.setOnClickListener {
            doLogin()
        }
//        binding.tvTour.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//
//        binding.tvFindAccount.setOnClickListener {
//            Toast.makeText(this, "找回密码", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun checkForm() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
//        val agree = binding.cbAgree.isChecked

        loginViewModel.loginDataChanged(username, password)
    }

    private fun doLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        binding.loading.visibility = View.VISIBLE

        // 调用 ViewModel 的登录方法
        loginViewModel.login(username, password)
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        Toast.makeText(this, "欢迎 ${model.displayName}", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoginFailed(@StringRes error: Int) {
        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
    }
}

// 扩展函数
fun EditText.afterTextChanged(block: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            block(s.toString())
        }
    })
}