package com.example.lab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import com.example.lab1.domain.model.User
import com.example.lab1.ui.component.EmailField
import com.example.lab1.ui.component.PasswordField
import com.example.lab1.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

class SignInFragment : Fragment() {

    private val userFlow: MutableStateFlow<User?> =
        MutableStateFlow(null)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        AppTheme {
            val user by userFlow.collectAsState()

            LaunchedEffect(Unit) {
                userFlow.value = arguments?.getParcelable("user", User::class.java)
            }

            SignInScreen(
                userName = user?.userName ?: "",
                userEmail = user?.email ?: "",
                onSignUp = {
                    MainActivity.navigateToFragment(
                        parentFragmentManager,
                        SignUpFragment()
                    )
                },
                onSignedIn = {
                    val bundle = bundleOf("name" to user?.userName)

                    MainActivity.navigateToFragment(
                        parentFragmentManager,
                        HomeFragment(),
                        bundle
                    )
                },
            )
        }
    }
}

@Composable
private fun SignInScreen(
    userName: String? = null,
    userEmail: String? = null,
    onSignUp: () -> Unit = {},
    onSignedIn: () -> Unit = {},
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
            userName?.let { name ->
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Приветствую $name",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
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
