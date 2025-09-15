package com.example.lab1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.lab1.ui.component.PasswordField
import com.example.lab1.ui.component.PrimaryButton
import com.example.lab1.ui.theme.AppTheme

class SignUpActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold { inner ->
                    SignUpScreen(
                        modifier = Modifier
                            .padding(inner)
                            .padding(24.dp),
                        onRegistered = { _, email ->
                            setResult(RESULT_OK, Intent().putExtra("email", email))
                            finish()
                        },
                        onBackToSignIn = {
                            startActivity(
                                Intent(
                                    this@SignUpActivity,
                                    SignInActivity::class.java,
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onBackToSignIn: () -> Unit = {},
    onRegistered: (name: String, email: String) -> Unit = { _, _ -> },
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf(true) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    val emailRegex = remember {
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$".toRegex(RegexOption.IGNORE_CASE)
    }

    fun validate(): Boolean {
        nameError = if (name.isBlank()) "Введите ваше имя" else null
        emailError = when {
            email.isBlank() -> "Почта обязательна"
            !emailRegex.matches(email) -> "Введите корректную почту"
            else -> null
        }
        passwordError = when {
            password.length < 8 -> "Минимум 8 символов"
            else -> null
        }
        confirmError = when {
            confirm.isBlank() -> "Необходимо подтвердить пароль"
            confirm != password -> "Пароль не совпадает"
            else -> null
        }
        return listOf(
            nameError,
            emailError,
            passwordError,
            confirmError
        ).all { it == null }
    }

    val canRegister by remember(name, email, password, confirm) {
        derivedStateOf {
            name.isNotBlank()
                    && email.isNotBlank()
                    && password.isNotBlank()
                    && confirm.isNotBlank()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = {
                name = it
                if (nameError != null) nameError = null
            },
            label = { Text("Имя") },
            isError = nameError != null,
            supportingText = { nameError?.let { Text(it) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = {
                email = it
                if (emailError != null) emailError = null
            },
            label = { Text("Почта") },
            isError = emailError != null,
            supportingText = { emailError?.let { Text(it) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
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
        PasswordField(
            label = "Подтвердить пароль",
            password = confirm,
            show = showConfirm,
            error = confirmError,
            onChange = { confirm = it; if (confirmError != null) confirmError = null },
            onToggleShow = { showConfirm = !showConfirm },
            modifier = Modifier
                .fillMaxWidth(),
        )

        MultiChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            SegmentedButton(
                checked = sex,
                onCheckedChange = { sex = it },
                shape = SegmentedButtonDefaults.itemShape(0, 2)
            ) {
                Text("Мужской")
            }
            SegmentedButton(
                checked = !sex,
                onCheckedChange = { sex = !it },
                shape = SegmentedButtonDefaults.itemShape(1, 2)
            ) {
                Text("Женский")
            }
        }

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Зарегистрироваться",
            enabled = canRegister,
            onClick = {
                if (validate()) {
                    onRegistered(name, email)
                }
            },
        )

        TextButton(onClick = onBackToSignIn) { Text("У вас уже есть аккаунт? Войдите!") }
    }
}
