package com.devarthur.setpoint.ui.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.application.usecase.LoginUseCase
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.ui.components.AppBarScreen
import com.devarthur.setpoint.ui.components.ErrorMessage
import com.devarthur.setpoint.ui.components.SetPointPrimaryButton
import kotlinx.coroutines.launch

private val EMAIL_REGEX = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
private const val MIN_PASSWORD_LENGTH = 6

@Composable
fun LoginScreen(
    role: Role,
    onSuccess: (userId: String, role: Role) -> Unit,
    onBack: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val title = if (role == Role.TRAINER) "Login professor" else "Login aluno"

    AppBarScreen(title = title, onBack = onBack) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; error = null },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; error = null },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                ErrorMessage(message = error!!, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (loading) {
                CircularProgressIndicator()
            } else {
                SetPointPrimaryButton(
                    onClick = {
                        val emailTrim = email.trim()
                        val passTrim = password
                        when {
                            emailTrim.isEmpty() -> error = "Informe o e-mail"
                            !EMAIL_REGEX.matches(emailTrim) -> error = "E-mail em formato inválido"
                            passTrim.length < MIN_PASSWORD_LENGTH -> error = "Senha deve ter no mínimo $MIN_PASSWORD_LENGTH caracteres"
                            else -> {
                                loading = true
                                error = null
                                scope.launch {
                                    val result = AppDependencies.loginUseCase(emailTrim, passTrim, role)
                                    loading = false
                                    result.fold(
                                        onSuccess = { loginResult ->
                                            onSuccess(loginResult.userId, loginResult.role)
                                        },
                                        onFailure = {
                                            error = LoginUseCase.INVALID_CREDENTIALS_MESSAGE
                                        },
                                    )
                                }
                            }
                        }
                    },
                    enabled = !loading,
                    text = "Entrar",
                )
            }
        }
    }
}
