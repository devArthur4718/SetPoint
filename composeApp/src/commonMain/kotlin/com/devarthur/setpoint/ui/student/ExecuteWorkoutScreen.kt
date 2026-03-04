package com.devarthur.setpoint.ui.student

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.util.TimeProvider
import com.devarthur.setpoint.ui.components.AppBarScreen
import com.devarthur.setpoint.ui.components.ErrorMessage
import com.devarthur.setpoint.ui.components.SetPointPrimaryButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExecuteWorkoutScreen(
    assignmentId: String,
    studentUserId: String,
    onSuccess: () -> Unit,
    onBack: () -> Unit,
) {
    var templateName by remember { mutableStateOf("") }
    var exerciseCount by remember { mutableStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(assignmentId) {
        val assignment = AppDependencies.workoutAssignmentRepository.getById(assignmentId)
        assignment?.let { a ->
            AppDependencies.workoutTemplateRepository.getById(a.workoutTemplateId)?.let { t ->
                templateName = t.name
                exerciseCount = t.exercises.size
            }
        }
    }

    AppBarScreen(
        title = "Executar treino",
        onBack = onBack,
        snackbarHostState = snackbarHostState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                "Treino: $templateName ($exerciseCount exercícios)",
                style = MaterialTheme.typography.titleMedium,
            )
            if (error != null) {
                ErrorMessage("Erro: $error", modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(16.dp))
            SetPointPrimaryButton(
                onClick = {
                    loading = true
                    error = null
                    scope.launch {
                        val executedAt = TimeProvider.currentTimeMillis()
                        val result = AppDependencies.recordWorkoutExecutionUseCase.execute(
                            workoutAssignmentId = assignmentId,
                            studentUserId = studentUserId,
                            setExecutionItems = emptyList(),
                            executedAt = executedAt,
                        )
                        loading = false
                        result.fold(
                            onSuccess = {
                                snackbarHostState.showSnackbar("Execução registrada com sucesso!")
                                delay(1500)
                                onSuccess()
                            },
                            onFailure = { error = it.message ?: "Erro" },
                        )
                    }
                },
                enabled = !loading,
                text = if (loading) "Salvando..." else "Concluir treino (sem séries)",
            )
        }
    }
}
