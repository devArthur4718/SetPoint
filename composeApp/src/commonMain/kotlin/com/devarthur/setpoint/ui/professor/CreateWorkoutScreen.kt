package com.devarthur.setpoint.ui.professor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.application.usecase.WorkoutTemplateItem
import com.devarthur.setpoint.domain.Exercise
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.ui.components.AppBarScreen
import com.devarthur.setpoint.ui.components.ErrorMessage
import com.devarthur.setpoint.ui.components.SetPointPrimaryButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateWorkoutScreen(
    trainerId: String,
    onSuccess: () -> Unit,
    onBack: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var exercises by remember { mutableStateOf(emptyList<Exercise>()) }
    val items = remember { mutableStateListOf<WorkoutTemplateItem>() }
    LaunchedEffect(Unit) {
        exercises = AppDependencies.exerciseRepository.list()
        if (items.isEmpty() && exercises.isNotEmpty()) {
            items.add(WorkoutTemplateItem(exerciseId = exercises.first().id, order = 1, sets = 3, reps = 10))
        }
    }

    AppBarScreen(
        title = "Criar treino",
        onBack = onBack,
        snackbarHostState = snackbarHostState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; error = null },
                label = { Text("Nome do treino") },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                "Exercícios (primeiro: ${exercises.firstOrNull()?.name ?: "nenhum"})",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp),
            )
            if (error != null) {
                ErrorMessage("Erro: $error", modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(16.dp))
            SetPointPrimaryButton(
                onClick = {
                    if (name.isBlank()) { error = "Nome obrigatório"; return@SetPointPrimaryButton }
                    if (items.isEmpty()) { error = "Adicione ao menos um exercício"; return@SetPointPrimaryButton }
                    loading = true
                    error = null
                    scope.launch {
                        val result = AppDependencies.createWorkoutTemplateUseCase.execute(
                            name = name.trim(),
                            trainerId = trainerId,
                            items = items.toList(),
                        )
                        loading = false
                        result.fold(
                            onSuccess = {
                                snackbarHostState.showSnackbar("Treino criado com sucesso!")
                                delay(1500)
                                onSuccess()
                            },
                            onFailure = { error = it.message ?: "Erro" },
                        )
                    }
                },
                enabled = !loading,
                text = if (loading) "Salvando..." else "Criar treino",
            )
        }
    }
}
