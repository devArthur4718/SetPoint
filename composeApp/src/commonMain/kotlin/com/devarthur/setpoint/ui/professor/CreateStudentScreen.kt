package com.devarthur.setpoint.ui.professor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.ui.components.AppBarScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateStudentScreen(
    trainerId: String,
    onSuccess: () -> Unit,
    onBack: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    AppBarScreen(title = "Criar aluno", onBack = onBack, snackbarHostState = snackbarHostState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; error = null },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; error = null },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Nome de exibição (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            if (error != null) {
                Text(
                    "Erro: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (email.isBlank() || name.isBlank()) {
                        error = "Preencha email e nome"
                        return@Button
                    }
                    loading = true
                    error = null
                    scope.launch {
                        val result = AppDependencies.createStudentUseCase.execute(
                            email = email.trim(),
                            name = name.trim(),
                            displayName = displayName.trim().takeIf { it.isNotEmpty() },
                            trainerId = trainerId,
                        )
                        loading = false
                        result.fold(
                            onSuccess = {
                                snackbarHostState.showSnackbar("Aluno criado com sucesso")
                                delay(2500)
                                onSuccess()
                            },
                            onFailure = { error = it.message ?: "Erro ao criar aluno" },
                        )
                    }
                },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (loading) "Salvando..." else "Criar aluno")
            }
        }
    }
}
