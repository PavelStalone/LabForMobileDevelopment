package com.example.lab1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab1.ui.component.EmailField
import com.example.lab1.ui.component.PasswordField
import com.example.lab1.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

class SignInActivity : ComponentActivity() {

    private val emailFlow = MutableStateFlow("")

    private val launcher = registerForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        runCatching {
            val data = result.data

            check(RESULT_OK == result.resultCode)
            checkNotNull(data)

            emailFlow.value = data.getStringExtra("email").toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                SignInScreen(
                    onSignUp = {
                        launcher.launch(
                            Intent(
                                this@SignInActivity,
                                SignUpActivity::class.java
                            )
                        )
                    },
                    onSignedIn = {
                        startActivity(
                            Intent(
                                this@SignInActivity,
                                HomeActivity::class.java
                            )
                        )
                    },
                    userEmail = emailFlow.collectAsState().value
                )
            }
        }
    }
}

@Composable
fun SignInScreen(
    onSignUp: () -> Unit = {},
    onSignedIn: () -> Unit = {},
    userEmail: String? = null,
) {
    var email by remember(userEmail) { mutableStateOf(userEmail ?: "") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val emailRegex = remember {
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$".toRegex(RegexOption.IGNORE_CASE)
    }

    val canSubmit by remember(email, password) {
        derivedStateOf { email.isNotBlank() && password.isNotBlank() }
    }

    fun validate(): Boolean {
        emailError = when {
            email.isBlank() -> "Почта обязательна"
            !emailRegex.matches(email) -> "Введите корректную почту"
            else -> null
        }
        passwordError = when {
            password.length < 8 -> "Минимум 8 символов"
            else -> null
        }
        return listOf(
            emailError,
            passwordError,
        ).all { it == null }
    }

    Scaffold { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EmailField(
                email = email,
                error = emailError,
                onChange = {
                    email = it

                    if (emailError != null) emailError = null
                },
                onClear = { email = "" },
                modifier = Modifier.fillMaxWidth()
            )

            PasswordField(
                modifier = Modifier.fillMaxWidth(),
                password = password,
                show = showPassword,
                error = passwordError,
                onChange = {
                    password = it
                    if (passwordError != null) passwordError = null
                },
                onToggleShow = { showPassword = !showPassword },
            )

            Button(
                onClick = {
                    if (validate()) onSignedIn()
                },
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Войти")
            }
            TextButton(onClick = onSignUp) { Text("Первый раз? Зарегистрируйся!") }
        }
    }
}
