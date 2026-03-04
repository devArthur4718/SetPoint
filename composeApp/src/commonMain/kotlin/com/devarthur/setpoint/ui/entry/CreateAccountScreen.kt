package com.devarthur.setpoint.ui.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
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
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.ui.components.AppBarScreen
import com.devarthur.setpoint.ui.components.ErrorMessage
import com.devarthur.setpoint.ui.components.SetPointPrimaryButton
import kotlinx.coroutines.launch

private val EMAIL_REGEX = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
private const val MIN_PASSWORD_LENGTH = 6
private const val NAME_MAX_LENGTH = 120

@Composable
fun CreateAccountScreen(
    initialRole: Role?,
    onSuccess: (Role) -> Unit,
    onBack: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(initialRole ?: Role.TRAINER) }
    var displayName by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    AppBarScreen(
        title = "Criar conta",
        onBack = onBack,
        snackbarHostState = snackbarHostState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
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
                label = { Text("Senha (mín. $MIN_PASSWORD_LENGTH caracteres)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; error = null },
                label = { Text("Confirmar senha") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it.take(NAME_MAX_LENGTH); error = null },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Papel",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                FilterChip(
                    selected = role == Role.TRAINER,
                    onClick = { role = Role.TRAINER },
                    label = { Text("Professor") },
                )
                Spacer(modifier = Modifier.height(8.dp))
                FilterChip(
                    selected = role == Role.STUDENT,
                    onClick = { role = Role.STUDENT },
                    label = { Text("Aluno") },
                )
            }
            if (role == Role.STUDENT) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Nome de exibição (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                ErrorMessage(message = error!!, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (loading) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            } else {
                SetPointPrimaryButton(
                    onClick = {
                        val emailTrim = email.trim()
                        val nameTrim = name.trim()
                        when {
                            emailTrim.isEmpty() -> error = "Informe o e-mail"
                            !EMAIL_REGEX.matches(emailTrim) -> error = "E-mail em formato inválido"
                            password.length < MIN_PASSWORD_LENGTH -> error = "Senha deve ter no mínimo $MIN_PASSWORD_LENGTH caracteres"
                            password != confirmPassword -> error = "As senhas não coincidem"
                            nameTrim.isEmpty() -> error = "Informe o nome"
                            nameTrim.length > NAME_MAX_LENGTH -> error = "Nome deve ter no máximo $NAME_MAX_LENGTH caracteres"
                            else -> {
                                loading = true
                                error = null
                                scope.launch {
                                    val result = AppDependencies.createAccountUseCase.execute(
                                        email = emailTrim,
                                        password = password,
                                        name = nameTrim,
                                        role = role,
                                        displayName = displayName.trim().takeIf { it.isNotEmpty() },
                                    )
                                    loading = false
                                    result.fold(
                                        onSuccess = {
                                            snackbarHostState.showSnackbar("Conta criada. Faça login.")
                                            onSuccess(it.role)
                                        },
                                        onFailure = { e -> error = e.message ?: "Erro ao criar conta" },
                                    )
                                }
                            }
                        }
                    },
                    enabled = !loading,
                    text = "Criar conta",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
