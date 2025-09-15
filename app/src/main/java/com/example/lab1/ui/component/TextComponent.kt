package com.example.lab1.ui.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.lab1.ui.icon.CustomIcons
import com.example.lab1.ui.icon.Visible
import com.example.lab1.ui.icon.VisibleOff

@Composable
fun PasswordField(
    password: String,
    show: Boolean,
    error: String?,
    onToggleShow: () -> Unit,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Пароль",
) {
    OutlinedTextField(
        value = password,
        onValueChange = onChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        singleLine = true,
        visualTransformation = if (show) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val icon =
                if (show) CustomIcons.Visible else CustomIcons.VisibleOff
            IconButton(onClick = onToggleShow) {
                Icon(icon, contentDescription = null)
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = modifier
    )
}

@Composable
fun EmailField(
    email: String,
    error: String?,
    onChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = email,
        onValueChange = onChange,
        label = { Text("Почта") },
        isError = error != null,
        supportingText = { error?.let { text -> Text(text = text) } },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        trailingIcon = {
            if (email.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Outlined.Close, contentDescription = null)
                }
            }
        },
        modifier = modifier
    )
}
